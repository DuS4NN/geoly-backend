package com.geoly.app.services;

import com.geoly.app.config.API;
import com.geoly.app.config.GeolyAPI;
import com.geoly.app.dao.Response;
import com.geoly.app.models.*;
import com.geoly.app.repositories.LanguageRepository;
import com.geoly.app.repositories.RoleRepository;
import com.geoly.app.repositories.TokenRepository;
import com.geoly.app.repositories.UserRepository;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.maxmind.geoip2.model.CityResponse;
import io.sentry.Sentry;
import net.bytebuddy.utility.RandomString;
import org.apache.commons.codec.digest.DigestUtils;
import org.jooq.DSLContext;
import org.jooq.Select;
import org.jooq.impl.DSL;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.io.File;
import java.net.InetAddress;
import java.util.*;

@Service
public class AccountService {

    private EntityManager entityManager;
    private DSLContext create;
    private API api;
    private NotificationService notificationService;
    private Argon2PasswordEncoder argon2PasswordEncoder;
    private LanguageRepository languageRepository;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private TokenRepository tokenRepository;

    public AccountService(API api, DSLContext create, EntityManager entityManager, NotificationService notificationService, Argon2PasswordEncoder argon2PasswordEncoder, LanguageRepository languageRepository, UserRepository userRepository, RoleRepository roleRepository, TokenRepository tokenRepository) {
        this.entityManager = entityManager;
        this.create = create;
        this.api = api;
        this.notificationService = notificationService;
        this.argon2PasswordEncoder = argon2PasswordEncoder;
        this.languageRepository = languageRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.tokenRepository = tokenRepository;
    }

    public Response findUser(String name){
        Select<?> query =
            create.select(com.geoly.app.jooq.tables.User.USER.NICK_NAME)
            .from(com.geoly.app.jooq.tables.User.USER)
            .where(com.geoly.app.jooq.tables.User.USER.ACTIVE.isTrue())
            .and(com.geoly.app.jooq.tables.User.USER.NICK_NAME.like("%"+name+"%"))
            .orderBy(
                DSL.when(com.geoly.app.jooq.tables.User.USER.NICK_NAME.like("%"+name), 1),
                DSL.when(com.geoly.app.jooq.tables.User.USER.NICK_NAME.like("%"+name+"%"), 2).otherwise(3)
            )
            .limit(5);

        Query q = entityManager.createNativeQuery(query.getSQL());
        GeolyAPI.setBindParameterValues(q, query);
        List result = q.getResultList();

        return new Response(StatusMessage.OK, HttpStatus.OK, result);
    }

    @Transactional(rollbackOn = Exception.class)
    public Response register(User user, int languageId, Coordinates coordinates){
        Optional<Language> language = languageRepository.findById(languageId);
        if(!language.isPresent()) return new Response(StatusMessage.LANGUAGE_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        Optional<User> userNickName = userRepository.findByNickName(user.getNickName());
        if(userNickName.isPresent()) return new Response(StatusMessage.NICKNAME_ALREADY_EXISTS, HttpStatus.METHOD_NOT_ALLOWED, null);

        Optional<User> userEmail = userRepository.findByEmail(user.getEmail());
        if(userEmail.isPresent()) return new Response(StatusMessage.EMAIL_ALREADY_EXISTS, HttpStatus.METHOD_NOT_ALLOWED, null);

        Optional<Role> role = roleRepository.findByName(RoleList.USER);
        if(!role.isPresent()) return new Response(StatusMessage.ROLE_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        User newUser = new User();
        newUser.setPassword(argon2PasswordEncoder.encode(user.getPassword()));
        newUser.setNickName(user.getNickName());
        newUser.setEmail(user.getEmail());
        newUser.setAbout("");
        newUser.setProfileImageUrl(API.userImageUrl+"default_profile_picture.png");
        newUser.setActive(true);
        newUser.setVerified(false);
        Set<Role> roles =  new HashSet<>();
        roles.add(role.get());
        newUser.setRole(roles);
        if(coordinates!=null) newUser.setAddress(coordinates.getCoordinates());
        entityManager.persist(newUser);

        UserOption userOption = new UserOption();
        userOption.setUser(newUser);
        userOption.setLanguage(language.get());
        userOption.setPrivateProfile(false);
        userOption.setMapTheme(1);
        userOption.setDarkMode(false);
        entityManager.persist(userOption);

        HashMap<String, Integer> data = new HashMap<>();
        data.put("userId", newUser.getId());
        notificationService.sendNotification(newUser, NotificationType.WELCOME, data, false);

        Token token = new Token();
        token.setAction(TokenType.CONFIRM_EMAIL);
        token.setUser(newUser);
        String tokenValue = DigestUtils.sha256Hex(user.getEmail() + RandomString.make(10));
        token.setToken(tokenValue);
        entityManager.persist(token);

        String emailText = "Verify your account \n localhost:3000/verify/"+tokenValue;
        api.sendEmail(emailText, user.getEmail(), "Email verification");

        return new Response(StatusMessage.USER_CREATED, HttpStatus.ACCEPTED, null);
    }

    public Coordinates findAddressFromIp(String ip){
        try{
            File database = new File("src/main/resources/GeoLite2-City.mmdb");
            DatabaseReader dbReader = new DatabaseReader.Builder(database).build();

            InetAddress ipAdress = InetAddress.getByName(ip);
            CityResponse response = dbReader.city(ipAdress);

            Coordinates coordinates = new Coordinates();
            coordinates.setLatitude(response.getLocation().getLatitude());
            coordinates.setLongitude(response.getLocation().getLongitude());

            return coordinates;
        }catch (AddressNotFoundException e){
            return null;
        }catch (Exception e){
            Sentry.capture(e);
            e.printStackTrace();
            return null;
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public Response verifyAccount(String tokenValue){
        Optional<Token> token = tokenRepository.findByTokenAndAction(tokenValue, TokenType.CONFIRM_EMAIL);
        if(!token.isPresent()) return new Response(StatusMessage.INVALID_TOKEN, HttpStatus.NOT_FOUND, null);

        User user = token.get().getUser();
        user.setVerified(true);

        entityManager.remove(token.get());
        entityManager.merge(user);

        return new Response(StatusMessage.ACCOUNT_ACTIVATED, HttpStatus.ACCEPTED, null);
    }

    @Transactional(rollbackOn = Exception.class)
    public Response sendResetPasswordEmail(String email){
        Optional<User> user = userRepository.findByEmail(email);
        if(!user.isPresent()) return new Response(StatusMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        Optional<Token> tokenOld = tokenRepository.findByUserAndAction(user.get(), TokenType.PASSWORD_RESET);
        if(tokenOld.isPresent()) entityManager.remove(tokenOld.get());

        if(!user.get().isVerified()) return new Response(StatusMessage.ACCOUNT_NOT_VERIFIED, HttpStatus.METHOD_NOT_ALLOWED, null);
        if(!user.get().isActive()) return new Response(StatusMessage.ACCOUNT_NOT_ACTIVE, HttpStatus.METHOD_NOT_ALLOWED, null);

        Token token = new Token();
        token.setAction(TokenType.PASSWORD_RESET);
        token.setUser(user.get());
        String tokenValue = DigestUtils.sha256Hex(user.get().getEmail() + RandomString.make(10));
        token.setToken(tokenValue);
        entityManager.persist(token);

        String emailText = "Reset your password \n localhost:3000/forgot/"+tokenValue;
        api.sendEmail(emailText, user.get().getEmail(), "Password reset");

        return new Response(StatusMessage.EMAIL_SENT, HttpStatus.ACCEPTED, null);
    }

    @Transactional(rollbackOn = Exception.class)
    public Response resetPassword(String tokenValue, String password){
        System.out.println(tokenValue);
        Optional<Token> token = tokenRepository.findByTokenAndAction(tokenValue, TokenType.PASSWORD_RESET);
        if(!token.isPresent()) return new Response(StatusMessage.INVALID_TOKEN, HttpStatus.NOT_FOUND, null);

        if(System.currentTimeMillis() - token.get().getCreatedAt().getTime() > 604800000){
            System.out.println(tokenValue);
            entityManager.remove(token.get());
            return new Response(StatusMessage.INVALID_TOKEN, HttpStatus.METHOD_NOT_ALLOWED, null);
        }

        User user = token.get().getUser();
        user.setPassword(argon2PasswordEncoder.encode(password));

        entityManager.remove(token.get());
        entityManager.merge(user);

        return new Response(StatusMessage.PASSWORD_RESET, HttpStatus.ACCEPTED, null);
    }
}

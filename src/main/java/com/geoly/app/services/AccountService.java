package com.geoly.app.services;

import com.geoly.app.config.API;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.File;
import java.net.InetAddress;
import java.util.*;

@Service
public class AccountService {

    private EntityManager entityManager;
    private API api;
    private Argon2PasswordEncoder argon2PasswordEncoder;
    private LanguageRepository languageRepository;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private TokenRepository tokenRepository;

    public AccountService(API api,EntityManager entityManager, Argon2PasswordEncoder argon2PasswordEncoder, LanguageRepository languageRepository, UserRepository userRepository, RoleRepository roleRepository, TokenRepository tokenRepository) {
        this.entityManager = entityManager;
        this.api = api;
        this.argon2PasswordEncoder = argon2PasswordEncoder;
        this.languageRepository = languageRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.tokenRepository = tokenRepository;
    }

    @Transactional(rollbackOn = Exception.class)
    public Response register(User user, int languageId, Coordinates coordinates){
        Optional<Language> language = languageRepository.findById(languageId);
        if(!language.isPresent()) return new Response(StatusMessage.LANGUAGE_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        Optional<User> userNickName = userRepository.findByNickName(user.getNickName());
        if(userNickName.isPresent()) return new Response(StatusMessage.NICKNAME_ALREADY_EXISTS, HttpStatus.NOT_ACCEPTABLE, null);

        Optional<User> userEmail = userRepository.findByEmail(user.getEmail());
        if(userEmail.isPresent()) return new Response(StatusMessage.EMAIL_ALREADY_EXISTS, HttpStatus.NOT_ACCEPTABLE, null);

        Optional<Role> role = roleRepository.findByName(RoleList.USER);
        if(!role.isPresent()) return new Response(StatusMessage.ROLE_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        User newUser = new User();
        newUser.setPassword(argon2PasswordEncoder.encode(user.getPassword()));
        newUser.setNickName(user.getNickName());
        newUser.setEmail(user.getEmail());
        newUser.setAbout("");
        newUser.setProfileImageUrl("src/main/resources/static/image/default_profile_picture.png");
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

        Token token = new Token();
        token.setAction(TokenType.CONFIRM_EMAIL);
        token.setUser(newUser);
        String tokenValue = DigestUtils.sha256Hex(user.getEmail() + RandomString.make(10));
        token.setToken(tokenValue);
        entityManager.persist(token);

        String emailText = "Verify your account \n localhost:8080/verify?token="+tokenValue;
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
    public List verifyAccount(String tokenValue){
        Optional<Token> token = tokenRepository.findByTokenAndAction(tokenValue, TokenType.CONFIRM_EMAIL);
        if(!token.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.INVALID_TOKEN, HttpStatus.NOT_FOUND));

        User user = token.get().getUser();
        user.setVerified(true);

        entityManager.remove(token.get());
        entityManager.merge(user);

        return Collections.singletonList(new ResponseEntity<>(StatusMessage.ACCOUNT_ACTIVATED, HttpStatus.OK));
    }

    @Transactional(rollbackOn = Exception.class)
    public List sendResetPasswordEmail(String email){
        Optional<User> user = userRepository.findByEmail(email);
        if(!user.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        if(!user.get().isVerified()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.ACCOUNT_NOT_VERIFIED, HttpStatus.METHOD_NOT_ALLOWED));
        if(!user.get().isActive()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.ACCOUNT_NOT_ACTIVE, HttpStatus.METHOD_NOT_ALLOWED));

        Token token = new Token();
        token.setAction(TokenType.PASSWORD_RESET);
        token.setUser(user.get());
        String tokenValue = DigestUtils.sha256Hex(user.get().getEmail() + RandomString.make(10));
        token.setToken(tokenValue);
        entityManager.persist(token);

        String emailText = "Reset your password \n localhost:8080/resetpassword?token="+tokenValue;
        api.sendEmail(emailText, user.get().getEmail(), "Password reset");

        return Collections.singletonList(new ResponseEntity<>(StatusMessage.EMAIL_SENT, HttpStatus.OK));
    }

    public List resetPassword(String tokenValue, String password){
        Optional<Token> token = tokenRepository.findByTokenAndAction(tokenValue, TokenType.PASSWORD_RESET);
        if(!token.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.INVALID_TOKEN, HttpStatus.NOT_FOUND));

        User user = token.get().getUser();
        user.setPassword(argon2PasswordEncoder.encode(password));

        entityManager.remove(token.get());
        entityManager.merge(user);

        return Collections.singletonList(new ResponseEntity<>(StatusMessage.PASSWORD_RESET, HttpStatus.OK));
    }
}

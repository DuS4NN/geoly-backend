package com.geoly.app.services;

import com.geoly.app.config.API;
import com.geoly.app.config.GeolyAPI;
import com.geoly.app.dao.Response;
import com.geoly.app.dao.Settings;
import com.geoly.app.models.Language;
import com.geoly.app.models.StatusMessage;
import com.geoly.app.models.User;
import com.geoly.app.models.UserOption;
import com.geoly.app.repositories.LanguageRepository;
import com.geoly.app.repositories.UserOptionRepository;
import com.geoly.app.repositories.UserRepository;
import com.tinify.Source;
import com.tinify.Tinify;
import org.jooq.DSLContext;
import org.jooq.Select;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class SettingsService {

    private EntityManager entityManager;
    private DSLContext create;
    private Argon2PasswordEncoder argon2PasswordEncoder;
    private UserRepository userRepository;
    private UserOptionRepository userOptionRepository;
    private LanguageRepository languageRepository;

    public SettingsService(EntityManager entityManager, DSLContext create, Argon2PasswordEncoder argon2PasswordEncoder, UserRepository userRepository, UserOptionRepository userOptionRepository, LanguageRepository languageRepository) {
        this.entityManager = entityManager;
        this.create = create;
        this.argon2PasswordEncoder = argon2PasswordEncoder;
        this.userRepository = userRepository;
        this.userOptionRepository = userOptionRepository;
        this.languageRepository = languageRepository;
    }

    public Response getSettings(int userId){
        Select<?> query =
            create.select(com.geoly.app.jooq.tables.UserOption.USER_OPTION.MAP_THEME, com.geoly.app.jooq.tables.UserOption.USER_OPTION.LANGUAGE_ID, com.geoly.app.jooq.tables.UserOption.USER_OPTION.PRIVATE_PROFILE, com.geoly.app.jooq.tables.User.USER.PROFILE_IMAGE_URL, com.geoly.app.jooq.tables.User.USER.ABOUT, com.geoly.app.jooq.tables.User.USER.ID)
                .from(com.geoly.app.jooq.tables.UserOption.USER_OPTION)
                .leftJoin(com.geoly.app.jooq.tables.User.USER)
                    .on(com.geoly.app.jooq.tables.User.USER.ID.eq(com.geoly.app.jooq.tables.UserOption.USER_OPTION.USER_ID))
                .where(com.geoly.app.jooq.tables.User.USER.ID.eq(userId));

        Query q = entityManager.createNativeQuery(query.getSQL());
        GeolyAPI.setBindParameterValues(q, query);
        List result = q.getResultList();

        if(result.isEmpty()) return new Response(StatusMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        return new Response(StatusMessage.OK, HttpStatus.OK, result);
    }

    @Transactional(rollbackOn = Exception.class)
    public Response toggleDarkMode(boolean toggle, int userId){
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()) return new Response(StatusMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        Optional<UserOption> userOption = userOptionRepository.findByUser(user.get());
        if(!userOption.isPresent()) return new Response(StatusMessage.USER_OPTION_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        userOption.get().setDarkMode(toggle);
        entityManager.merge(userOption.get());

        return new Response(StatusMessage.DARK_MODE_CHANGED, HttpStatus.ACCEPTED, null);
    }

    @Transactional(rollbackOn = Exception.class)
    public Response changeSettings(Settings settings, int userId){
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()) return new Response(StatusMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        Optional<UserOption> userOption = userOptionRepository.findByUser(user.get());
        if(!userOption.isPresent()) return new Response(StatusMessage.USER_OPTION_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        Optional<Language> language = languageRepository.findById(settings.getLanguageId());
        if(!language.isPresent()) return new Response(StatusMessage.LANGUAGE_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        user.get().setAbout(settings.getAbout());
        entityManager.merge(user.get());

        userOption.get().setPrivateProfile(settings.isPrivateProfile());
        userOption.get().setLanguage(language.get());
        entityManager.merge(userOption.get());

        return new Response(StatusMessage.SETTINGS_UPDATED, HttpStatus.ACCEPTED, null);
    }

    @Transactional(rollbackOn = Exception.class)
    public Response setProfileImage(MultipartFile file, int userId) throws Exception{
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()) return new Response(StatusMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND, null);


        File dir = new File(API.userImageUrl+userId);
        if(!dir.exists()){
            dir.mkdirs();
        }else{
        File oldImage = new File(API.userImageUrl+userId+"/"+userId+".jpg");
        oldImage.delete();
        }

        Source source = Tinify.fromBuffer(file.getBytes());
        source.toFile(API.userImageUrl+userId+"/"+userId+".jpg");

        user.get().setProfileImageUrl(API.userImageUrl+userId+"/"+userId+".jpg");
        entityManager.merge(user.get());

        return new Response(StatusMessage.PROFILE_IMAGE_SET, HttpStatus.ACCEPTED, null);
    }

    @Transactional(rollbackOn = Exception.class)
    public Response deleteProfileImage(int userId){
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()) return new Response(StatusMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND, null);

        user.get().setProfileImageUrl(API.userImageUrl+"default_profile_picture.png");
        entityManager.merge(user.get());

        return new Response(StatusMessage.PROFILE_IMAGE_DELETED, HttpStatus.ACCEPTED, null);
    }

    public List changePassword(String oldPassword, String newPassword, int userId){
        Optional<User> user = userRepository.findByIdAndPassword(userId, argon2PasswordEncoder.encode(oldPassword));
        if(!user.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.INCORRECT_OLD_PASSWORD, HttpStatus.METHOD_NOT_ALLOWED));

        user.get().setPassword(argon2PasswordEncoder.encode(newPassword));
        entityManager.merge(user.get());

        return Collections.singletonList(new ResponseEntity<>(StatusMessage.PASSWORD_CHANGED, HttpStatus.OK));
    }
}

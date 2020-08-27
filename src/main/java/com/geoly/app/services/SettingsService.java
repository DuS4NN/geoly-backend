package com.geoly.app.services;

import com.geoly.app.config.API;
import com.geoly.app.models.Language;
import com.geoly.app.models.StatusMessage;
import com.geoly.app.models.User;
import com.geoly.app.models.UserOption;
import com.geoly.app.repositories.LanguageRepository;
import com.geoly.app.repositories.UserOptionRepository;
import com.geoly.app.repositories.UserRepository;
import com.tinify.Source;
import com.tinify.Tinify;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class SettingsService {

    private EntityManager entityManager;
    private Argon2PasswordEncoder argon2PasswordEncoder;
    private UserRepository userRepository;
    private UserOptionRepository userOptionRepository;
    private LanguageRepository languageRepository;

    public SettingsService(EntityManager entityManager, Argon2PasswordEncoder argon2PasswordEncoder, UserRepository userRepository, UserOptionRepository userOptionRepository, LanguageRepository languageRepository) {
        this.entityManager = entityManager;
        this.argon2PasswordEncoder = argon2PasswordEncoder;
        this.userRepository = userRepository;
        this.userOptionRepository = userOptionRepository;
        this.languageRepository = languageRepository;
    }

    @Transactional(rollbackOn = Exception.class)
    public List changeSettings(UserOption newUserOption, String about, int languagId, int userId){
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        Optional<UserOption> userOption = userOptionRepository.findByUser(user.get());
        if(!userOption.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_OPTION_NOT_FOUND, HttpStatus.NOT_FOUND));

        Optional<Language> language = languageRepository.findById(languagId);
        if(!language.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.LANGUAGE_NOT_FOUND, HttpStatus.NOT_FOUND));

        user.get().setAbout(about);
        entityManager.merge(user.get());

        userOption.get().setDarkMode(newUserOption.isDarkMode());
        userOption.get().setMapTheme(newUserOption.getMapTheme());
        userOption.get().setPrivateProfile(newUserOption.isPrivateProfile());
        userOption.get().setLanguage(language.get());
        entityManager.merge(userOption.get());

        return Collections.singletonList(new ResponseEntity<>(StatusMessage.SETTINGS_UPDATED, HttpStatus.OK));
    }

    @Transactional(rollbackOn = Exception.class)
    public List setProfileImage(MultipartFile file, int userId) throws Exception{
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND));


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

        return Collections.singletonList(new ResponseEntity<>(StatusMessage.PROFILE_IMAGE_SET, HttpStatus.OK));
    }

    public List deleteProfileImage(int userId){
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        user.get().setProfileImageUrl(API.userImageUrl+"default_profile_picture.svg");
        entityManager.merge(user.get());

        return Collections.singletonList(new ResponseEntity<>(StatusMessage.PROFILE_IMAGE_DELETED, HttpStatus.OK));
    }

    public List changePassword(String oldPassword, String newPassword, int userId){
        Optional<User> user = userRepository.findByIdAndPassword(userId, argon2PasswordEncoder.encode(oldPassword));
        if(!user.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.INCORRECT_OLD_PASSWORD, HttpStatus.METHOD_NOT_ALLOWED));

        user.get().setPassword(argon2PasswordEncoder.encode(newPassword));
        entityManager.merge(user.get());

        return Collections.singletonList(new ResponseEntity<>(StatusMessage.PASSWORD_CHANGED, HttpStatus.OK));
    }
}

package com.geoly.app.services;

import com.geoly.app.models.StatusMessage;
import com.geoly.app.models.User;
import com.geoly.app.repositories.UserRepository;
import com.tinify.Source;
import com.tinify.Tinify;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private UserRepository userRepository;

    public SettingsService(EntityManager entityManager, UserRepository userRepository) {
        this.entityManager = entityManager;
        this.userRepository = userRepository;
    }

    @Transactional(rollbackOn = Exception.class)
    public List setProfileImage(MultipartFile file, int userId){
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        try{
            File dir = new File("src/main/resources/static/image/user_profile_image/"+userId);
            if(!dir.exists()){
                dir.mkdirs();
            }else{
            File oldImage = new File("src/main/resources/static/image/user_profile_image/"+userId+"/"+userId+".jpg");
            oldImage.delete();
            }

            Source source = Tinify.fromBuffer(file.getBytes());
            source.toFile("src/main/resources/static/image/user_profile_image/"+userId+"/"+userId+".jpg");

            user.get().setProfileImageUrl("src/main/resources/static/image/user_profile_image/"+userId+".jpg");
            entityManager.merge(user.get());
        }catch (Exception e){
            e.printStackTrace();
        }
        return Collections.singletonList(new ResponseEntity<>(StatusMessage.PROFILE_IMAGE_SET, HttpStatus.OK));
    }

    public List deleteProfileImage(int userId){
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        user.get().setProfileImageUrl("src/main/resources/static/image/default_profile_picture.png");
        entityManager.merge(user.get());

        return Collections.singletonList(new ResponseEntity<>(StatusMessage.PROFILE_IMAGE_DELETED, HttpStatus.OK));
    }
}

package com.geoly.app.controler;

import com.geoly.app.config.GeolyAPI;
import com.geoly.app.models.CustomUserDetails;
import com.geoly.app.services.SettingsService;
import com.geoly.app.validators.Validator;
import com.geoly.app.validators.ValidatorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@RestController
public class SettingsController {

    private SettingsService settingsService;
    private Validator validator;

    public SettingsController(SettingsService settingsService, Validator validator) {
        this.settingsService = settingsService;
        this.validator = validator;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/settings/setimage")
    public List setProfileImage(@RequestParam MultipartFile file, Authentication authentication){
        ValidatorResponse validatorResponse = validator.imageValidator(file.getContentType(), file.getSize());
        if(!validatorResponse.isValid()) return Collections.singletonList(new ResponseEntity<>(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus()));

        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return settingsService.setProfileImage(file, customUserDetails.getUser().getId());
        }catch (Exception e){
            return GeolyAPI.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/settings/deleteimage")
    public List deleteProfileImage(Authentication authentication){
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return settingsService.deleteProfileImage(customUserDetails.getUser().getId());
        }catch (Exception e){
            return GeolyAPI.catchException(e);
        }
    }
}

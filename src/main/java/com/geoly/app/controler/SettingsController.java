package com.geoly.app.controler;

import com.geoly.app.config.API;
import com.geoly.app.config.GeolyAPI;
import com.geoly.app.dao.Response;
import com.geoly.app.dao.Settings;
import com.geoly.app.models.CustomUserDetails;
import com.geoly.app.models.Language;
import com.geoly.app.models.UserOption;
import com.geoly.app.repositories.LanguageRepository;
import com.geoly.app.services.SettingsService;
import com.geoly.app.validators.Validator;
import com.geoly.app.validators.ValidatorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@RestController
public class SettingsController {

    private SettingsService settingsService;
    private LanguageRepository languageRepository;
    private Validator validator;

    public SettingsController(SettingsService settingsService, LanguageRepository languageRepository, Validator validator) {
        this.settingsService = settingsService;
        this.languageRepository = languageRepository;
        this.validator = validator;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getsettings")
    public Response getSettings(Authentication authentication){
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return settingsService.getSettings(customUserDetails.getUser().getId());
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @GetMapping("/getlanguages")
    public List<Language> getLanguages(){
        return languageRepository.findAll();
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/settings")
    public Response changeSettings(@RequestBody @Validated Settings settings, Authentication authentication){
        ValidatorResponse validatorResponse = validator.changeSettingsValidator(settings);
        if(!validatorResponse.isValid()) return new Response(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus(), null);

        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return settingsService.changeSettings(settings, customUserDetails.getUser().getId());
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/settings/deleteimage")
    public Response deleteProfileImage(Authentication authentication){
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return settingsService.deleteProfileImage(customUserDetails.getUser().getId());
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/settings/setimage")
    public Response setProfileImage(@RequestParam MultipartFile file, Authentication authentication){
        ValidatorResponse validatorResponse = validator.imageValidator(file.getContentType(), file.getSize());
        if(!validatorResponse.isValid()) return new Response(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus(), null);

        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return settingsService.setProfileImage(file, customUserDetails.getUser().getId());
        }catch (Exception e){
            return API.catchException(e);
        }
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("settings/darkmode")
    public Response toggleDarkMode(@RequestParam(name="toggle") boolean toggle, Authentication authentication){
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return settingsService.toggleDarkMode(toggle, customUserDetails.getUser().getId());
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/settings/changepassword")
    public List changePassword(@RequestParam(name="newPassword") String newPassword, @RequestParam(name="oldPassword") String oldPassword, Authentication authentication){
        ValidatorResponse validatorResponse = validator.changePasswordValidator(newPassword);
        if(!validatorResponse.isValid()) return Collections.singletonList(new ResponseEntity<>(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus()));

        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return settingsService.changePassword(oldPassword, newPassword, customUserDetails.getUser().getId());
        }catch (Exception e){
            return GeolyAPI.catchException(e);
        }
    }
}
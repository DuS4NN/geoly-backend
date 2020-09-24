package com.geoly.app.controler;

import com.geoly.app.config.API;
import com.geoly.app.config.GeolyAPI;
import com.geoly.app.dao.Response;
import com.geoly.app.models.CustomUserDetails;
import com.geoly.app.models.StatusMessage;
import com.geoly.app.models.UserReportReason;
import com.geoly.app.services.ProfileService;
import com.geoly.app.validators.Validator;
import com.geoly.app.validators.ValidatorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class ProfileController {

    private ProfileService profileService;
    private Validator validator;

    public ProfileController(ProfileService profileService, Validator validator){
        this.profileService = profileService;
        this.validator = validator;
    }

    @GetMapping(path = "/profile")
    public Response getProfile(@RequestParam(name = "nickName") String nickName){
        ValidatorResponse validatorResponse = validator.getProfileValidator(nickName);
        if(!validatorResponse.isValid()) return new Response(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus(), null);

        try{
            List<List> profile = new ArrayList<>();

            profile.add(profileService.getUserDetail(nickName));
            if(profile.get(0).isEmpty()) return new Response(StatusMessage.USER_NOT_FOUND, HttpStatus.BAD_REQUEST, null);

            profile.add(profileService.getUserBadges(nickName));
            profile.add(profileService.getUserQuests(nickName));
            profile.add(profileService.getUserPlayedQuests(nickName));
            profile.add(profileService.getUserActivity(nickName));

            return new Response(StatusMessage.OK, HttpStatus.OK, profile);
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(path = "/profile/{nickName}/report")
    public List reportUser(@PathVariable(name = "nickName") String nickName, @RequestParam(name = "reason") UserReportReason userReportReason, Authentication authentication){
        ValidatorResponse validatorResponse = validator.getProfileValidator(nickName);
        if(!validatorResponse.isValid()) return Collections.singletonList(new ResponseEntity<>(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus()));
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return profileService.reportUser(nickName, userReportReason, customUserDetails.getUser().getId());
        }catch (Exception e){
            return GeolyAPI.catchException(e);
        }
    }
}

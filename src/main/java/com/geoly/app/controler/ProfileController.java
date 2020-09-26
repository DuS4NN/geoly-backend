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

    @GetMapping(path = "/profile/finished")
    public Response getFinished(@RequestParam(name = "nickName") String nickName, @RequestParam(name = "page") int page){
        try{
            return new Response(StatusMessage.OK, HttpStatus.OK, profileService.getUserPlayedQuests(nickName, page));
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @GetMapping(path = "/profile/created")
    public Response getCreated(@RequestParam(name = "nickName") String nickName, @RequestParam(name = "page") int page){
        try{
            return new Response(StatusMessage.OK, HttpStatus.OK, profileService.getUserQuests(nickName, page));
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @GetMapping(path = "profile/reportreason")
    public UserReportReason[] getQuestReportReasons(){
        return UserReportReason.values();
    }

    @GetMapping(path = "/profile")
    public Response getProfile(@RequestParam(name = "nickName") String nickName, Authentication authentication){
        ValidatorResponse validatorResponse = validator.getProfileValidator(nickName);
        if(!validatorResponse.isValid()) return new Response(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus(), null);

        try{
            List<List> profile = new ArrayList<>();

            if(authentication != null){
                CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
                profile.add(profileService.getUserDetail(nickName, customUserDetails.getUser().getId()));
            }else{

                profile.add(profileService.getUserDetail(nickName,0));
            }
            if(profile.get(0).isEmpty()) return new Response(StatusMessage.USER_NOT_FOUND, HttpStatus.BAD_REQUEST, null);

            profile.add(profileService.getUserBadges(nickName));
            profile.add(profileService.getUserQuests(nickName, 1));
            profile.add(profileService.getUserPlayedQuests(nickName, 1));
            profile.add(profileService.getUserActivity(nickName));
            profile.add(profileService.getFinishedCount(nickName));
            profile.add(profileService.getCreatedCount(nickName));


            return new Response(StatusMessage.OK, HttpStatus.OK, profile);
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/profile/report")
    public Response reportUser(@RequestParam(name = "id") int id, @RequestParam(name = "reason") UserReportReason userReportReason, Authentication authentication){
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return profileService.reportUser(id, userReportReason, customUserDetails.getUser().getId());
        }catch (Exception e){
            return API.catchException(e);
        }
    }
}

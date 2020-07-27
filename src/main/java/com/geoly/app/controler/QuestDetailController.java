package com.geoly.app.controler;

import com.geoly.app.config.GeolyAPI;
import com.geoly.app.models.CustomUserDetails;
import com.geoly.app.models.QuestReportReason;
import com.geoly.app.models.QuestReview;
import com.geoly.app.models.StatusMessage;
import com.geoly.app.services.QuestDetailService;
import com.geoly.app.validators.Validator;
import com.geoly.app.validators.ValidatorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class QuestDetailController {

    private QuestDetailService questDetailService;
    private Validator validator;

    public QuestDetailController(QuestDetailService questDetailService, Validator validator){
        this.questDetailService = questDetailService;
        this.validator = validator;
    }

    @GetMapping(path = "quest/{id}")
    public List getDetailsOfQuest(@PathVariable(name = "id") int id){
        ValidatorResponse validatorResponse = validator.checkOnlyId(id);
        if(!validatorResponse.isValid()) return Collections.singletonList(new ResponseEntity<>(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus()));

        try{
            List<List> questDetails = new ArrayList<>();

            questDetails.add(questDetailService.getDetailsOfQuest(id));

            if(questDetails.get(0).isEmpty()) return Collections.singletonList(new ResponseEntity<>(StatusMessage.QUEST_NOT_FOUND, HttpStatus.BAD_REQUEST));

            questDetails.add(questDetailService.getReviewsOfQuest(id));
            questDetails.add(questDetailService.getStagesOfQuest(id));
            questDetails.add(questDetailService.getImagesOfQuest(id));

            return questDetails;
        }catch (Exception e){
            return GeolyAPI.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("quest/{id}/review")
    public List createReview(@PathVariable(name = "id") int id, @Validated @RequestBody QuestReview questReview, Authentication authentication){
        ValidatorResponse validatorResponse = validator.createReviewValidator(id, questReview);
        if(!validatorResponse.isValid()) return Collections.singletonList(new ResponseEntity<>(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus()));

        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return questDetailService.createReview(customUserDetails.getUser().getId(), id, questReview);
        }catch (Exception e){
            return GeolyAPI.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("quest/{id}/review")
    public List removeReview(@PathVariable(name = "id") int id, @RequestParam(name = "reviewId") int reviewId, Authentication authentication){
        ValidatorResponse validatorResponse = validator.removeReviewValidator(id, reviewId);
        if(!validatorResponse.isValid()) return Collections.singletonList(new ResponseEntity<>(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus()));

        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return questDetailService.removeReview(customUserDetails.getUser().getId(), id, reviewId);
        }catch (Exception e){
            return GeolyAPI.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("quest/{id}/review")
    public List updateReview(@PathVariable(name = "id") int id, @Validated @RequestBody QuestReview questReview, Authentication authentication){
        ValidatorResponse validatorResponse = validator.updateReviewValidator(id, questReview);
        if(!validatorResponse.isValid()) return Collections.singletonList(new ResponseEntity<>(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus()));

        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return questDetailService.updateReview(customUserDetails.getUser().getId(), id, questReview);
        }catch (Exception e){
            return GeolyAPI.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("quest/{id}/signin")
    public List signUpOnQuest(@PathVariable(name = "id") int id, Authentication authentication){
        ValidatorResponse validatorResponse = validator.checkOnlyId(id);
        if(!validatorResponse.isValid()) return Collections.singletonList(new ResponseEntity<>(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus()));

        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return questDetailService.signUpOnQuest(customUserDetails.getUser().getId(), id);
        }catch (Exception e){
            return GeolyAPI.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("quest/{id}/signout")
    public List signOutOfQuest(@PathVariable(name = "id") int id, Authentication authentication){
        ValidatorResponse validatorResponse = validator.checkOnlyId(id);
        if(!validatorResponse.isValid()) return Collections.singletonList(new ResponseEntity<>(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus()));

        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return questDetailService.signOutOfQuest(customUserDetails.getUser().getId(), id);
        }catch (Exception e){
            return GeolyAPI.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("quest/{id}/report")
    public List reportQuest(@PathVariable(name = "id") int id, Authentication authentication, @RequestParam(name = "reason") QuestReportReason reason){
        ValidatorResponse validatorResponse = validator.checkOnlyId(id);
        if(!validatorResponse.isValid()) return Collections.singletonList(new ResponseEntity<>(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus()));

        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return questDetailService.reportQuest(customUserDetails.getUser().getId(), id, reason);
        }catch (Exception e){
            return GeolyAPI.catchException(e);
        }
    }
}
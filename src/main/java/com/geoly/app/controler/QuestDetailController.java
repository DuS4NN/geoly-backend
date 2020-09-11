package com.geoly.app.controler;

import com.geoly.app.config.API;
import com.geoly.app.config.GeolyAPI;
import com.geoly.app.dao.Response;
import com.geoly.app.models.CustomUserDetails;
import com.geoly.app.models.QuestReportReason;
import com.geoly.app.models.QuestReview;
import com.geoly.app.services.QuestDetailService;
import com.geoly.app.validators.Validator;
import com.geoly.app.validators.ValidatorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(path = "quest/images")
    public Response getQuestImages(@RequestParam(name = "id") int id){
        ValidatorResponse validatorResponse = validator.checkOnlyId(id);
        if(!validatorResponse.isValid()) return new Response(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus(), null);

        try{
            return questDetailService.getImagesOfQuest(id);
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @GetMapping(path = "quest/review")
    public Response getQuestReviews(@RequestParam(name = "id") int id, @RequestParam(name = "page") int page, Authentication authentication){
        ValidatorResponse validatorResponse = validator.checkOnlyId(id);
        if(!validatorResponse.isValid()) return new Response(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus(), null);

        try{
            if(authentication != null){
                CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
                return questDetailService.getReviewsOfQuest(id, customUserDetails.getUser().getId(), page);
            }
            return questDetailService.getReviewsOfQuest(id, 0, page);
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @GetMapping(path = "quest/reviewinfo")
    public Response getReviewCountAndWritable(@RequestParam(name = "id") int id, Authentication authentication){
        try{
            if(authentication != null){
                CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
                return questDetailService.getReviewCountAndWritable(id, customUserDetails.getUser().getId());
            }else{
                return questDetailService.getReviewCountAndWritable(id,0);
            }
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @GetMapping(path = "quest/stage")
    public Response getQuestStages(@RequestParam(name = "id") int id){
        ValidatorResponse validatorResponse = validator.checkOnlyId(id);
        if(!validatorResponse.isValid()) return new Response(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus(), null);

        try{
            return questDetailService.getStagesOfQuest(id);
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @GetMapping(path = "quest/detail")
    public Response getQuestDetails(@RequestParam(name = "id") int id){
        ValidatorResponse validatorResponse = validator.checkOnlyId(id);
        if(!validatorResponse.isValid()) return new Response(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus(), null);

        try{
            return questDetailService.getDetailsOfQuest(id);
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("quest/review")
    public Response removeReview(@RequestParam(name = "reviewId") int reviewId, Authentication authentication){
        ValidatorResponse validatorResponse = validator.removeReviewValidator(reviewId);
        if(!validatorResponse.isValid()) return new Response(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus(), null);

        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return questDetailService.removeReview(customUserDetails.getUser().getId(), reviewId);
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("quest/review")
    public Response updateReview(@Validated @RequestBody QuestReview questReview, @RequestParam(name = "questId") int questId, Authentication authentication){
        ValidatorResponse validatorResponse = validator.updateReviewValidator(questReview);
        if(!validatorResponse.isValid()) return new Response(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus(), null);

        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return questDetailService.updateReview(customUserDetails.getUser().getId(), questReview, questId);
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("quest/review")
    public Response createReview(@RequestParam(name = "id") int id, @Validated @RequestBody QuestReview questReview, Authentication authentication){
        ValidatorResponse validatorResponse = validator.createReviewValidator(id, questReview);
        if(!validatorResponse.isValid()) return new Response(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus(), null);

        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return questDetailService.createReview(customUserDetails.getUser().getId(), id, questReview);
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("quest/signin")
    public Response signUpOnQuest(@RequestParam(name = "id") int id, Authentication authentication){
        ValidatorResponse validatorResponse = validator.checkOnlyId(id);
        if(!validatorResponse.isValid()) return new Response(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus(), null);

        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return questDetailService.signUpOnQuest(customUserDetails.getUser().getId(), id);
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("quest/report")
    public Response reportQuest(@RequestParam(name = "id") int id, Authentication authentication, @RequestParam(name = "reason") QuestReportReason reason){
        ValidatorResponse validatorResponse = validator.checkOnlyId(id);
        if(!validatorResponse.isValid()) return new Response(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus(), null);

        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return questDetailService.reportQuest(customUserDetails.getUser().getId(), id, reason);
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @GetMapping(path = "quest/reportreason")
    public QuestReportReason[] getQuestReportReasons(){
        return QuestReportReason.values();
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
}
package com.geoly.app.controler;

import com.geoly.app.config.GeolyAPI;
import com.geoly.app.dao.EditQuest;
import com.geoly.app.models.CustomUserDetails;
import com.geoly.app.services.QuestService;
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
public class QuestController {

    private QuestService questService;
    private Validator validator;

    public QuestController(QuestService questService, Validator validator) {
        this.questService = questService;
        this.validator = validator;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/signindaily")
    public List signInDailyQuest(Authentication authentication){
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return questService.signInDailyQuest(customUserDetails.getUser().getId());
        }catch (Exception e){
            return GeolyAPI.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/quest/{id}/image")
    public List editQuestImages(@RequestParam List<MultipartFile> files, @PathVariable(name = "id") int questId, Authentication authentication){
        ValidatorResponse validatorResponse = validator.imagesValidator(files, questId);
        if(!validatorResponse.isValid()) return Collections.singletonList(new ResponseEntity<>(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus()));

        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return questService.editQuestImage(files, customUserDetails.getUser().getId(), questId);
        }catch (Exception e){
            return GeolyAPI.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/quest/{id}/edit")
    public List editQuestDetails(@RequestBody @Validated EditQuest questDetails, @PathVariable(name = "id") int questId, Authentication authentication){
        ValidatorResponse validatorResponse = validator.editQuest(questDetails, questId);
        if(!validatorResponse.isValid()) return Collections.singletonList(new ResponseEntity<>(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus()));

        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return questService.editQuestDetails(questDetails, questId, customUserDetails.getUser().getId());
        }catch (Exception e){
            return GeolyAPI.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/quest/{id}/delete")
    public List deleteQuest(@PathVariable(name = "id") int questId, Authentication authentication){
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return questService.doActionWithQuest(questId, customUserDetails.getUser().getId(), "DELETE");
        }catch (Exception e){
            return GeolyAPI.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/quest/{id}/activate")
    public List activateQuest(@PathVariable(name = "id") int questId, Authentication authentication){
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return questService.doActionWithQuest(questId, customUserDetails.getUser().getId(), "ACTIVATE");
        }catch (Exception e){
            return GeolyAPI.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/quest/{id}/disable")
    public List disableQuest(@PathVariable(name = "id") int questId, Authentication authentication){
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return questService.doActionWithQuest(questId, customUserDetails.getUser().getId(), "DISABLE");
        }catch (Exception e){
            return GeolyAPI.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/quest/{id}/editor")
    public List getQuestForEdit(@PathVariable(name = "id") int questId, Authentication authentication){
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return questService.getQuestForEdit(questId, customUserDetails.getUser().getId());
        }catch (Exception e){
            return GeolyAPI.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/allcreatedquests")
    public List getAllCreatedQuests(Authentication authentication){
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return questService.getAllCreatedQuests(customUserDetails.getUser().getId());
        }catch (Exception e){
            return GeolyAPI.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/allactivequests")
    public List getAllActiveQuests(Authentication authentication){
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return questService.getAllActiveQuests(customUserDetails.getUser().getId());
        }catch (Exception e){
            return GeolyAPI.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/dailyquest")
    public List getDailyQuest(Authentication authentication){
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return questService.getDailyQuest(customUserDetails.getUser().getId());
        }catch (Exception e){
            return GeolyAPI.catchException(e);
        }
    }
}

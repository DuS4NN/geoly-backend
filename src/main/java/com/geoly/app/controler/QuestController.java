package com.geoly.app.controler;

import com.geoly.app.config.API;
import com.geoly.app.config.GeolyAPI;
import com.geoly.app.dao.EditQuest;
import com.geoly.app.dao.Response;
import com.geoly.app.models.CustomUserDetails;
import com.geoly.app.services.QuestService;
import com.geoly.app.validators.Validator;
import com.geoly.app.validators.ValidatorResponse;
import org.springframework.http.MediaType;
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
    @GetMapping("/activequest")
    public Response getActiveQuest(Authentication authentication){
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return questService.getActiveQuest(customUserDetails.getUser().getId());
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/allcreatedquests")
    public Response getAllCreatedQuests(Authentication authentication){
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return questService.getAllCreatedQuests(customUserDetails.getUser().getId());
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/allplayedquests")
    public Response getAllPlayedQuests(@RequestParam(name = "page") int page, Authentication authentication){
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return questService.getAllPlayedQuests(customUserDetails.getUser().getId(), page);
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/countplayedquests")
    public int getCountPlayedQuests(Authentication authentication){
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        return questService.getCountPlayedQuests(customUserDetails.getUser().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/quest/disable")
    public Response disableQuest(@RequestParam(name = "id") int questId, Authentication authentication){
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return questService.doActionWithQuest(questId, customUserDetails.getUser().getId(), "DISABLE");
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/quest/editdetail")
    public Response editQuestDetails(@RequestBody @Validated EditQuest questDetails, @RequestParam (name = "id") int questId, Authentication authentication){
        ValidatorResponse validatorResponse = validator.editQuest(questDetails, questId);
        if(!validatorResponse.isValid()) return new Response(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus(), null);

        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return questService.editQuestDetails(questDetails, questId, customUserDetails.getUser().getId());
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/quest/getimages")
    public Response getQuestImagesForEdit(@RequestParam(name = "id") int questId, Authentication authentication){
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return questService.getQuestImagesForEdit(questId, customUserDetails.getUser().getId());
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/quest/editimage")
    public Response editQuestImages(@RequestParam List<MultipartFile> files, @RequestParam(name = "id") int questId, @RequestParam(name = "deleted") int[] deleted, Authentication authentication){
        ValidatorResponse validatorResponse = validator.imagesValidator(files, questId);
        if(!validatorResponse.isValid()) return new Response(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus(), null);

        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return questService.editQuestImage(files, customUserDetails.getUser().getId(), questId, deleted);
        }catch (Exception e){
            return API.catchException(e);
        }
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
    @GetMapping("/quest/{id}/delete")
    public Response deleteQuest(@PathVariable(name = "id") int questId, Authentication authentication){
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return questService.doActionWithQuest(questId, customUserDetails.getUser().getId(), "DELETE");
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/quest/{id}/activate")
    public Response activateQuest(@PathVariable(name = "id") int questId, Authentication authentication){
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return questService.doActionWithQuest(questId, customUserDetails.getUser().getId(), "ACTIVATE");
        }catch (Exception e){
            return API.catchException(e);
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

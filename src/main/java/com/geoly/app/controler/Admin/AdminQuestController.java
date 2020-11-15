package com.geoly.app.controler.Admin;

import com.geoly.app.config.API;
import com.geoly.app.dao.AddQuest;
import com.geoly.app.dao.AdminEditQuest;
import com.geoly.app.dao.Response;
import com.geoly.app.models.CustomUserDetails;
import com.geoly.app.services.Admin.AdminQuestService;
import com.geoly.app.validators.Validator;
import com.geoly.app.validators.ValidatorResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class AdminQuestController {

    private AdminQuestService adminQuestService;
    private Validator validator;

    public AdminQuestController(AdminQuestService adminQuestService, Validator validator) {
        this.adminQuestService = adminQuestService;
        this.validator = validator;
    }

    @PreAuthorize("hasAnyRole('MOD, ADMIN')")
    @GetMapping("/adminQuest")
    public Response getQuests(@RequestParam(name = "name") String name, @RequestParam(name = "page") int page){
        try{
            return adminQuestService.getQuests(name, page);
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("hasAnyRole('MOD, ADMIN')")
    @PostMapping("/adminUpdateImages")
    public Response updateImages(@RequestParam List<MultipartFile> files, @RequestParam(name = "id") int questId, @RequestParam(name = "deleted") int[] deleted, Authentication authentication){
        ValidatorResponse validatorResponse = validator.imagesValidator(files, questId);
        if(!validatorResponse.isValid()) return new Response(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus(), null);

        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return adminQuestService.updateImages(files, customUserDetails.getUser().getId(), questId, deleted);
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("hasAnyRole('MOD, ADMIN')")
    @GetMapping("/adminQuestDetails")
    public Response getQuestDetails(@RequestParam(name = "id") int id){
        try{
            return adminQuestService.getQuestDetails(id);
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("hasAnyRole('MOD, ADMIN')")
    @GetMapping("/adminQuestPlayed")
    public Response getQuestPlayed(@RequestParam(name = "id") int id, @RequestParam(name = "page") int page, @RequestParam(name = "userId") int userId){
        try{
            return adminQuestService.getQuestPlayed(id, page, userId);
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("hasAnyRole('MOD, ADMIN')")
    @PostMapping("/adminEditQuest")
    public Response editQuest(@RequestBody @Validated AdminEditQuest adminEditQuest, Authentication authentication){
        ValidatorResponse validatorResponse = validator.adminEditQuest(adminEditQuest);
        if (!validatorResponse.isValid()) return new Response(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus(), null);
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return adminQuestService.editQuest(adminEditQuest, customUserDetails.getUser().getId());
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("hasAnyRole('MOD, ADMIN')")
    @PostMapping("/adminAddQuest")
    public Response addQuest(@RequestBody AddQuest quest, Authentication authentication) {
        ValidatorResponse validatorResponse = validator.addQuest(quest);
        if (!validatorResponse.isValid()) return new Response(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus(), null);

        try {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return adminQuestService.addQuest(quest, customUserDetails.getUser().getId());
        } catch (Exception e) {
            return API.catchException(e);
        }
    }

}

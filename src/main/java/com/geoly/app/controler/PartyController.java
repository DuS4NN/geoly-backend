package com.geoly.app.controler;

import com.geoly.app.config.API;
import com.geoly.app.dao.Response;
import com.geoly.app.models.CustomUserDetails;
import com.geoly.app.models.StatusMessage;
import com.geoly.app.services.PartyService;
import com.geoly.app.validators.Validator;
import com.geoly.app.validators.ValidatorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
public class PartyController {

    private Validator validator;
    private PartyService partyService;

    public PartyController(Validator validator, PartyService partyService) {
        this.validator = validator;
        this.partyService = partyService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/createdgroups")
    public Response getCreatedParties(Authentication authentication, @RequestParam(name = "page") int page){
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return partyService.getCreatedParties(customUserDetails.getUser().getId(), page);
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/countenteredgroups")
    public int getCountOfEnteredGroups(Authentication authentication){
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        return partyService.getCountOfEnteredParties(customUserDetails.getUser().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/countcreatedgroups")
    public int getCountOfCreatedParties(Authentication authentication){
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        return partyService.getCountOfCreatedParties(customUserDetails.getUser().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/group/delete")
    public Response deleteParty(@RequestParam(name = "id") int partyId, Authentication authentication){
        ValidatorResponse validatorResponse = validator.checkOnlyId(partyId);
        if(!validatorResponse.isValid()) return new Response(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus(), null);

        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return partyService.deleteParty(partyId, customUserDetails.getUser().getId());
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/group/create")
    public Response createParty(@RequestParam(name = "name") String name, Authentication authentication){
        ValidatorResponse validatorResponse = validator.checkOnlyNameOfParty(name);
        if(!validatorResponse.isValid()) return new Response(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus(), null);

        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return partyService.createParty(name, customUserDetails.getUser().getId());
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/group/quests")
    public Response getPartyQuests(@RequestParam(name = "id") int id, Authentication authentication){
        ValidatorResponse validatorResponse = validator.checkOnlyId(id);
        if(!validatorResponse.isValid()) return new Response(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus(), null);
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return partyService.getPartyQuests(id, customUserDetails.getUser().getId());
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/group/deletequest")
    public Response deleteQuest(@RequestParam(name = "partyId") int partyId, @RequestParam(name = "questId") int questId, Authentication authentication){
        ValidatorResponse validatorResponse = validator.checkOnlyId(partyId);
        if(!validatorResponse.isValid()) return new Response(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus(), null);

        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return partyService.deleteQuest(partyId, questId, customUserDetails.getUser().getId());
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/group/edit")
    public Response editQuest(@RequestParam(name = "partyId") int partyId, @RequestParam(name = "name") String name, Authentication authentication){
        ValidatorResponse validatorResponse = validator.checkOnlyNameOfParty(name);
        if(!validatorResponse.isValid()) return new Response(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus(), null);

        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return partyService.editGroup(partyId, customUserDetails.getUser().getId(), name);
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/group/users")
    public Response getUsersInGroup(@RequestParam(name = "id") int partyId, Authentication authentication){
        ValidatorResponse validatorResponse = validator.checkOnlyId(partyId);
        if(!validatorResponse.isValid()) return new Response(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus(), null);

        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return partyService.getUsersInGroup(partyId, customUserDetails.getUser().getId());
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/group/kick")
    public Response kickUserFromParty(@RequestParam(name = "partyId") int partyId, @RequestParam(name = "userId") int userId, Authentication authentication){
        ValidatorResponse validatorResponse = validator.checkOnlyId(partyId);
        if(!validatorResponse.isValid()) return new Response(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus(), null);

        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            if(customUserDetails.getUser().getId() == userId) return new Response(StatusMessage.CAN_NOT_KICK_OWNER, HttpStatus.METHOD_NOT_ALLOWED, null);
            return partyService.kickUserFromParty(partyId, userId, customUserDetails.getUser().getId());
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/group/invite")
    public Response inviteUser(@RequestParam(name = "partyId") int partyId, @RequestParam(name = "nickName") String nickName, Authentication authentication){
        ValidatorResponse validatorResponse = validator.checkOnlyId(partyId);
        if(!validatorResponse.isValid()) return new Response(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus(), null);

        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return  partyService.inviteUser(partyId, nickName, customUserDetails.getUser().getId());
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/group/getallcreated")
    public Response getAllCreatedParties (Authentication authentication){
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return partyService.getAllCreatedParties(customUserDetails.getUser().getId());
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/group/addquest")
    public Response addQuest(@RequestParam(name = "partyId") int partyId, @RequestParam(name = "questId") int questId, Authentication authentication){
        ValidatorResponse validatorResponse = validator.checkOnlyId(partyId);
        if(!validatorResponse.isValid()) return new Response(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus(), null);

        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return partyService.addQuest(partyId, questId, customUserDetails.getUser().getId());
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/enteredgroups")
    public Response getEnteredParties(Authentication authentication, @RequestParam(name = "page") int page){
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return partyService.getEnteredParties(customUserDetails.getUser().getId(), page);
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/group/leave")
    public Response leaveParty(@RequestParam(name = "id") int partyId, Authentication authentication){
        ValidatorResponse validatorResponse = validator.checkOnlyId(partyId);
        if(!validatorResponse.isValid()) return new Response(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus(), null);

        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return partyService.leaveParty(partyId, customUserDetails.getUser().getId());
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/group")
    public Response getPartyDetails(@RequestParam(name = "id") int partyId, Authentication authentication){
        ValidatorResponse validatorResponse = validator.checkOnlyId(partyId);
        if(!validatorResponse.isValid()) return new Response(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus(), null);

        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return partyService.getPartyDetails(partyId, customUserDetails.getUser().getId());
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/group/questdetails")
    public Response getPartyQuestDetails(@RequestParam(name = "partyId") int partyId, @RequestParam(name = "questId") int questId, Authentication authentication){
        ValidatorResponse validatorResponse = validator.checkOnlyId(partyId);
        if(!validatorResponse.isValid()) return new Response(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus(), null);

        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return partyService.getPartyQuestDetails(partyId, questId, customUserDetails.getUser().getId());
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/group/signin")
    public Response signInPartyQuest(@RequestParam(name = "partyId") int partyId, @RequestParam(name = "questId") int questId, Authentication authentication){
        ValidatorResponse validatorResponse = validator.checkOnlyId(partyId);
        if(!validatorResponse.isValid()) return new Response(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus(), null);

        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return partyService.signInPartyQuest(partyId, questId, customUserDetails.getUser().getId());
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/group/signout")
    public Response signOutPartyQuest(@RequestParam(name = "partyId") int partyId, @RequestParam(name = "questId") int questId, Authentication authentication){
        ValidatorResponse validatorResponse = validator.checkOnlyId(partyId);
        if(!validatorResponse.isValid()) return new Response(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus(), null);

        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return partyService.signOutPartyQuest(partyId, questId, customUserDetails.getUser().getId());
        }catch (Exception e){
            return API.catchException(e);
        }
    }
}
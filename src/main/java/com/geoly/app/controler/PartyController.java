package com.geoly.app.controler;

import com.geoly.app.config.API;
import com.geoly.app.config.GeolyAPI;
import com.geoly.app.dao.Response;
import com.geoly.app.models.CustomUserDetails;
import com.geoly.app.models.StatusMessage;
import com.geoly.app.services.PartyService;
import com.geoly.app.validators.Validator;
import com.geoly.app.validators.ValidatorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

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
    @GetMapping("/groups")
    public List getAllParties(Authentication authentication){
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return partyService.getAllParties(customUserDetails.getUser().getId());
        }catch (Exception e){
            return GeolyAPI.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/group/{id}/leave")
    public List leaveParty(@PathVariable(name = "id") int partyId, Authentication authentication){
        ValidatorResponse validatorResponse = validator.checkOnlyId(partyId);
        if(!validatorResponse.isValid()) return Collections.singletonList(new ResponseEntity<>(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus()));

        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return partyService.leaveParty(partyId, customUserDetails.getUser().getId());
        }catch (Exception e){
            return GeolyAPI.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/group/create")
    public List createParty(@RequestParam(name = "name") String name, Authentication authentication){
        ValidatorResponse validatorResponse = validator.checkOnlyNameOfParty(name);
        if(!validatorResponse.isValid()) return Collections.singletonList(new ResponseEntity<>(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus()));

        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return partyService.createParty(name, customUserDetails.getUser().getId());
        }catch (Exception e){
            return GeolyAPI.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/group/{id}")
    public List getPartyDetails(@PathVariable(name = "id") int partyId, Authentication authentication){
        ValidatorResponse validatorResponse = validator.checkOnlyId(partyId);
        if(!validatorResponse.isValid()) return Collections.singletonList(new ResponseEntity<>(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus()));

        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return partyService.getPartyDetails(partyId, customUserDetails.getUser().getId());
        }catch (Exception e){
            return GeolyAPI.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/group/{partyId}/kick/{userId}")
    public List kickUserFromParty(@PathVariable(name = "partyId") int partyId, @PathVariable(name = "userId") int userId, Authentication authentication){
        ValidatorResponse validatorResponse = validator.checkOnlyId(partyId);
        if(!validatorResponse.isValid()) return Collections.singletonList(new ResponseEntity<>(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus()));

        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            if(customUserDetails.getUser().getId() == userId) return Collections.singletonList(new ResponseEntity<>(StatusMessage.CAN_NOT_KICK_OWNER, HttpStatus.OK));
            return partyService.kickUserFromParty(partyId, userId, customUserDetails.getUser().getId());
        }catch (Exception e){
            return GeolyAPI.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/group/{id}/invite/{nickName}")
    public List inviteUser(@PathVariable(name = "id") int partyId, @PathVariable(name = "nickName") String nickName, Authentication authentication){
        ValidatorResponse validatorResponse = validator.inviteUserValidator(partyId, nickName);
        if(!validatorResponse.isValid()) return Collections.singletonList(new ResponseEntity<>(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus()));

        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return  partyService.inviteUser(partyId, nickName, customUserDetails.getUser().getId());
        }catch (Exception e){
            return GeolyAPI.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/group/{partyId}/quest/{questId}/add")
    public List addQuest(@PathVariable(name = "partyId") int partyId, @PathVariable(name = "questId") int questId, Authentication authentication){
        ValidatorResponse validatorResponse = validator.checkOnlyId(partyId);
        if(!validatorResponse.isValid()) return Collections.singletonList(new ResponseEntity<>(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus()));

        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return partyService.addQuest(partyId, questId, customUserDetails.getUser().getId());
        }catch (Exception e){
            return GeolyAPI.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/group/{partyId}/quest/{questId}/activate")
    public List activateQuest(@PathVariable(name = "partyId") int partyId, @PathVariable(name = "questId") int questId, Authentication authentication){
        ValidatorResponse validatorResponse = validator.checkOnlyId(partyId);
        if(!validatorResponse.isValid()) return Collections.singletonList(new ResponseEntity<>(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus()));

        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return partyService.changeActiveQuest(partyId, questId, customUserDetails.getUser().getId(), "ACTIVATE");
        }catch (Exception e){
            return GeolyAPI.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/group/{partyId}/quest/{questId}/deactivate")
    public List deactivateQuest(@PathVariable(name = "partyId") int partyId, @PathVariable(name = "questId") int questId, Authentication authentication){
        ValidatorResponse validatorResponse = validator.checkOnlyId(partyId);
        if(!validatorResponse.isValid()) return Collections.singletonList(new ResponseEntity<>(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus()));

        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return partyService.changeActiveQuest(partyId, questId, customUserDetails.getUser().getId(), "DEACTIVATE");
        }catch (Exception e){
            return GeolyAPI.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/group/{partyId}/quest/{questId}/signin")
    public List signInPartyQuest(@PathVariable(name = "partyId") int partyId, @PathVariable(name = "questId") int questId, Authentication authentication){
        ValidatorResponse validatorResponse = validator.checkOnlyId(partyId);
        if(!validatorResponse.isValid()) return Collections.singletonList(new ResponseEntity<>(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus()));

        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return partyService.signInPartyQuest(partyId, questId, customUserDetails.getUser().getId());
        }catch (Exception e){
            return GeolyAPI.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/group/{partyId}/quest/{questId}/signout")
    public List signOutPartyQuest(@PathVariable(name = "partyId") int partyId, @PathVariable(name = "questId") int questId, Authentication authentication){
        ValidatorResponse validatorResponse = validator.checkOnlyId(partyId);
        if(!validatorResponse.isValid()) return Collections.singletonList(new ResponseEntity<>(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus()));

        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return partyService.signOutPartyQuest(partyId, questId, customUserDetails.getUser().getId());
        }catch (Exception e){
            return GeolyAPI.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/group/{partyId}/quest/{questId}/details")
    public List getPartyQuestDetails(@PathVariable(name = "partyId") int partyId, @PathVariable(name = "questId") int questId, Authentication authentication){
        ValidatorResponse validatorResponse = validator.checkOnlyId(partyId);
        if(!validatorResponse.isValid()) return Collections.singletonList(new ResponseEntity<>(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus()));

        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return partyService.getPartyQuestDetails(partyId, questId, customUserDetails.getUser().getId());
        }catch (Exception e){
            return GeolyAPI.catchException(e);
        }
    }
}
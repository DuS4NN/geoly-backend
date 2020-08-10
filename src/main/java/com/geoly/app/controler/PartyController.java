package com.geoly.app.controler;

import com.geoly.app.config.GeolyAPI;
import com.geoly.app.models.CustomUserDetails;
import com.geoly.app.services.PartyService;
import com.geoly.app.validators.Validator;
import com.geoly.app.validators.ValidatorResponse;
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
    @GetMapping("/group")
    public List getAllParties(Authentication authentication){
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return partyService.getAllParties(customUserDetails.getUser().getId());
        }catch (Exception e){
            return GeolyAPI.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/group/{id}/leave")
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
    @GetMapping("/group/{id}/delete")
    public List deleteParty(@PathVariable(name = "id") int partyId, Authentication authentication){
        ValidatorResponse validatorResponse = validator.checkOnlyId(partyId);
        if(!validatorResponse.isValid()) return Collections.singletonList(new ResponseEntity<>(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus()));

        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return partyService.deleteParty(partyId, customUserDetails.getUser().getId());
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
}

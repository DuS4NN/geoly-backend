package com.geoly.app.controler;

import com.geoly.app.config.GeolyAPI;
import com.geoly.app.models.CustomUserDetails;
import com.geoly.app.services.InviteService;
import com.geoly.app.validators.Validator;
import com.geoly.app.validators.ValidatorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class InviteController {

    private Validator validator;
    private InviteService inviteService;

    public InviteController(Validator validator, InviteService inviteService) {
        this.validator = validator;
        this.inviteService = inviteService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/invite")
    public List getPendingInvites(Authentication authentication){
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return inviteService.getPendingInvites(customUserDetails.getUser().getId());
        }catch (Exception e){
            return GeolyAPI.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/invite/{id}/cancel")
    public List cancelInvite(@PathVariable(name = "id") int inviteId, Authentication authentication){
        ValidatorResponse validatorResponse = validator.checkOnlyId(inviteId);
        if(!validatorResponse.isValid()) return Collections.singletonList(new ResponseEntity<>(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus()));

        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return inviteService.cancelInvite(inviteId, customUserDetails.getUser().getId());
        }catch (Exception e){
            return GeolyAPI.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/invite/{id}/accept")
    public List acceptInvite(@PathVariable(name = "id") int inviteId, Authentication authentication){
        ValidatorResponse validatorResponse = validator.checkOnlyId(inviteId);
        if(!validatorResponse.isValid()) return Collections.singletonList(new ResponseEntity<>(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus()));

        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return inviteService.acceptInvite(inviteId, customUserDetails.getUser().getId());
        }catch (Exception e){
            return GeolyAPI.catchException(e);
        }
    }

}

package com.geoly.app.controler;

import com.geoly.app.config.API;
import com.geoly.app.dao.Response;
import com.geoly.app.models.CustomUserDetails;
import com.geoly.app.services.InviteService;
import com.geoly.app.validators.Validator;
import com.geoly.app.validators.ValidatorResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
    public Response getPendingInvites(@RequestParam(name = "count") int count, Authentication authentication){
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return inviteService.getPendingInvites(customUserDetails.getUser().getId(), count);
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/invite/setunseen")
    public void setUnseen( Authentication authentication){
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        inviteService.setUnseen(customUserDetails.getUser().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/invite/cancel")
    public Response cancelInvite(@RequestParam(name = "id") int inviteId, Authentication authentication){
        ValidatorResponse validatorResponse = validator.checkOnlyId(inviteId);
        if(!validatorResponse.isValid()) return new Response(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus(), null);

        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return inviteService.cancelInvite(inviteId, customUserDetails.getUser().getId());
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/invite/accept")
    public Response acceptInvite(@RequestParam(name = "id") int inviteId, Authentication authentication){
        ValidatorResponse validatorResponse = validator.checkOnlyId(inviteId);
        if(!validatorResponse.isValid()) return new Response(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus(), null);

        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return inviteService.acceptInvite(inviteId, customUserDetails.getUser().getId());
        }catch (Exception e){
            return API.catchException(e);
        }
    }

}

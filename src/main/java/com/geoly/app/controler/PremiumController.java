package com.geoly.app.controler;

import com.geoly.app.config.API;
import com.geoly.app.dao.Response;
import com.geoly.app.models.CustomUserDetails;
import com.geoly.app.models.StatusMessage;
import com.geoly.app.services.PremiumService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
public class PremiumController {

    private PremiumService premiumService;

    public PremiumController(PremiumService premiumService) {
        this.premiumService = premiumService;
    }

    @GetMapping("/premium/cancel")
    public List cancelPayment(){
        return null;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/premium/success")
    public Response successPayment(@RequestParam(name = "token") String token, Authentication authentication){
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return premiumService.executeAgreement(token, customUserDetails.getUser().getId());
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/premium")
    public Response createSubscription(Authentication authentication){
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            String planId = premiumService.createSubscription(customUserDetails.getUser().getId());

            if(planId.equals(StatusMessage.USER_ALREADY_HAS_PREMIUM.name())) return new Response(StatusMessage.USER_ALREADY_HAS_PREMIUM, HttpStatus.METHOD_NOT_ALLOWED, null);
            if(planId.equals(StatusMessage.USER_NOT_FOUND.name())) return new Response(StatusMessage.USER_NOT_FOUND, HttpStatus.BAD_REQUEST, null);

            String url = premiumService.createAgreement(planId);
            if(url != null){
                return new Response(StatusMessage.OK, HttpStatus.OK, Collections.singletonList(url));
            }else{
                return new Response(StatusMessage.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR, null);

            }
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/premium/unsubscribe")
    public Response cancelSubscription(Authentication authentication){
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return premiumService.cancelAgreement(customUserDetails.getUser().getId());
        }catch (Exception e){
            return API.catchException(e);
        }
    }
}

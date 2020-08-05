package com.geoly.app.controler;

import com.geoly.app.config.GeolyAPI;
import com.geoly.app.jooq.Geoly;
import com.geoly.app.models.CustomUserDetails;
import com.geoly.app.models.StatusMessage;
import com.geoly.app.services.PremiumService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public List successPayment(@RequestParam(name = "token") String token, Authentication authentication){
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return premiumService.executeAgreement(token, customUserDetails.getUser().getId());
        }catch (Exception e){
            return GeolyAPI.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/premium")
    public List createSubscription(Authentication authentication){
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            String planId = premiumService.createSubscription(customUserDetails.getUser().getId());

            if(planId.equals(StatusMessage.USER_ALREADY_HAS_PREMIUM.name())) return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_ALREADY_HAS_PREMIUM, HttpStatus.METHOD_NOT_ALLOWED));
            if(planId.equals(StatusMessage.USER_NOT_FOUND.name())) return Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_NOT_FOUND, HttpStatus.BAD_REQUEST));

            String url = premiumService.createAgreement(planId);
            if(url != null){
                return Collections.singletonList(new ResponseEntity<>(url, HttpStatus.OK));
            }else{
                return Collections.singletonList(new ResponseEntity<>(StatusMessage.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR));

            }
        }catch (Exception e){
            return GeolyAPI.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/premium/unsubscribe")
    public List cancelSubscription(Authentication authentication){
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return premiumService.cancelAgreement(customUserDetails.getUser().getId());
        }catch (Exception e){
            return GeolyAPI.catchException(e);
        }
    }
}

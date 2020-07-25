package com.geoly.app.controler;

import com.geoly.app.config.GeolyAPI;
import com.geoly.app.models.Coordinates;
import com.geoly.app.models.User;
import com.geoly.app.services.AccountService;
import com.geoly.app.validators.Validator;
import com.geoly.app.validators.ValidatorResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

@RestController
public class AccountController {

    private Validator validator;
    private AccountService accountService;

    public AccountController(Validator validator, AccountService accountService){
        this.validator = validator;
        this.accountService = accountService;
    }

    @PostMapping("/register")
    public List register(@Validated @RequestBody User user, @RequestParam(name = "languageId") int languageId, HttpServletRequest request){
        ValidatorResponse validatorResponse = validator.registerValidator(user, languageId);
        if(!validatorResponse.isValid()) return Collections.singletonList(new ResponseEntity<>(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus()));

        try{
            Coordinates coordinates = accountService.findAddressFromIp(request.getRemoteAddr());
            return accountService.register(user, languageId, coordinates);
        }catch (Exception e){
            return GeolyAPI.catchException(e);
        }
    }

    @GetMapping("/verify")
    public List verifyAccount(@RequestParam(name = "token") String token){
        try{
            return accountService.verifyAccount(token);
        }catch (Exception e){
            e.printStackTrace();
            return null;
            //return GeolyAPI.catchException(e);
        }
    }

    public List resetPassword(){
        return null;
    }

}

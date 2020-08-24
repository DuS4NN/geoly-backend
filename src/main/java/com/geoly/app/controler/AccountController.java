package com.geoly.app.controler;

import com.geoly.app.config.API;
import com.geoly.app.config.GeolyAPI;
import com.geoly.app.dao.Response;
import com.geoly.app.models.Coordinates;
import com.geoly.app.models.User;
import com.geoly.app.services.AccountService;
import com.geoly.app.validators.Validator;
import com.geoly.app.validators.ValidatorResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    public Response register(@Validated @RequestBody User user, @RequestParam(name = "languageId") int languageId, HttpServletRequest request){
        ValidatorResponse validatorResponse = validator.registerValidator(user, languageId);
        if(!validatorResponse.isValid()) return new Response(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus(), null);

        try{
            Coordinates coordinates = accountService.findAddressFromIp(request.getRemoteAddr());
            return accountService.register(user, languageId, coordinates);
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @GetMapping("/verify")
    public List verifyAccount(@RequestParam(name = "token") String token){
        try{
            return accountService.verifyAccount(token);
        }catch (Exception e){
            return GeolyAPI.catchException(e);
        }
    }

    @GetMapping("/reset")
    public Response sendResetPasswordEmail(@RequestParam("email") String email){
        ValidatorResponse validatorResponse = validator.checkOnlyEmail(email);
        if(!validatorResponse.isValid()) return new Response(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus(), null);

        try{
            return accountService.sendResetPasswordEmail(email);
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @GetMapping("/resetpassword")
    public List resetPassword(@RequestParam("token") String token, @RequestParam("password") String password){
        try{
            return accountService.resetPassword(token, password);
        }catch (Exception e){
            return GeolyAPI.catchException(e);
        }
    }
}

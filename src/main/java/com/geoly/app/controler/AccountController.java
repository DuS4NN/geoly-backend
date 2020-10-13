package com.geoly.app.controler;

import com.geoly.app.config.API;
import com.geoly.app.dao.Response;
import com.geoly.app.models.Coordinates;
import com.geoly.app.models.CustomUserDetails;
import com.geoly.app.models.StatusMessage;
import com.geoly.app.models.User;
import com.geoly.app.services.AccountService;
import com.geoly.app.validators.Validator;
import com.geoly.app.validators.ValidatorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class AccountController {

    private Validator validator;
    private AccountService accountService;

    public AccountController(Validator validator, AccountService accountService){
        this.validator = validator;
        this.accountService = accountService;
    }


    @GetMapping("checkUser")
    public Response checkUser(Authentication authentication){
        try{
            if(authentication == null){
                return new Response(StatusMessage.USER_NOT_LOGGED_IN, HttpStatus.UNAUTHORIZED, null);
            }else{
                CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
                return accountService.checkUser(customUserDetails.getUser().getId());
            }
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @GetMapping("/finduser")
    public Response findUser(@RequestParam(name = "nick") String nick){
        try{
            return accountService.findUser(nick);
        }catch (Exception e){
            return API.catchException(e);
        }
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
    public Response verifyAccount(@RequestParam(name = "token") String token){
        try{
            return accountService.verifyAccount(token);
        }catch (Exception e){
            return API.catchException(e);
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
    public Response resetPassword(@RequestParam("token") String token, @RequestParam("password") String password){
        ValidatorResponse validatorResponse = validator.checkOnlyPassword(password);
        if(!validatorResponse.isValid()) return new Response(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus(), null);

        try{
            return accountService.resetPassword(token, password);
        }catch (Exception e){
            return API.catchException(e);
        }
    }
}

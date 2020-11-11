package com.geoly.app.controler.Admin;

import com.geoly.app.config.API;
import com.geoly.app.dao.AdminEditUser;
import com.geoly.app.dao.IntegerList;
import com.geoly.app.dao.Response;
import com.geoly.app.models.CustomUserDetails;
import com.geoly.app.models.Role;
import com.geoly.app.repositories.RoleRepository;
import com.geoly.app.services.Admin.AdminUserService;
import com.geoly.app.validators.Validator;
import com.geoly.app.validators.ValidatorResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AdminUserController {


    private AdminUserService adminUserService;
    private Validator validator;
    private RoleRepository roleRepository;

    public AdminUserController(AdminUserService adminUserService, Validator validator, RoleRepository roleRepository) {
        this.adminUserService = adminUserService;
        this.validator = validator;
        this.roleRepository = roleRepository;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/adminRoles")
    public List<Role> getRoles(){
        return roleRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/adminUpdateRoles")
    public Response updateRoles(@RequestBody IntegerList roles, @RequestParam(name = "id") int id, Authentication authentication){
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return adminUserService.updateRoles(roles, id, customUserDetails.getUser().getId());
        }catch (Exception e){
            return API.catchException(e);
        }
    }


    @PreAuthorize("hasAnyRole('MOD, ADMIN')")
    @GetMapping("/adminUser")
    public Response getUsers(@RequestParam(name = "nick") String nick, @RequestParam(name = "page") int page){
        try{
            return adminUserService.getUsers(nick, page);
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("hasAnyRole('MOD, ADMIN')")
    @GetMapping("/adminUserDetail")
    public Response getUser(@RequestParam(name = "id") int id){
        try{
            return adminUserService.getUser(id);
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("hasAnyRole('MOD, ADMIN')")
    @GetMapping("/adminRemoveBadge")
    public Response removeBadge(@RequestParam(name = "id") int id, Authentication authentication){
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

            return adminUserService.removeBadge(id, customUserDetails.getUser().getId());
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("hasAnyRole('MOD, ADMIN')")
    @GetMapping("/adminRemoveReview")
    public Response removeReview(@RequestParam(name = "id") int id, Authentication authentication){
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

            return adminUserService.removeReview(id, customUserDetails.getUser().getId());
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("hasAnyRole('MOD, ADMIN')")
    @GetMapping("/adminRemoveProfileImage")
    public Response removeImage(@RequestParam(name = "id") int id, Authentication authentication){
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

            return adminUserService.removeImage(id, customUserDetails.getUser().getId());
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("hasAnyRole('MOD, ADMIN')")
    @PostMapping("/adminEditUser")
    public Response editUser(@RequestBody @Validated AdminEditUser adminEditUser, Authentication authentication){
        ValidatorResponse validatorResponse = validator.editUser(adminEditUser);
        if (!validatorResponse.isValid()) return new Response(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus(), null);
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

            return adminUserService.editUser(adminEditUser, customUserDetails.getUser().getId());
        }catch (Exception e){
            return API.catchException(e);
        }
    }
}

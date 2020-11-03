package com.geoly.app.controler.Admin;

import com.geoly.app.config.API;
import com.geoly.app.dao.Response;
import com.geoly.app.models.CustomUserDetails;
import com.geoly.app.services.Admin.AdminUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminUserController {


    private AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
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
    @GetMapping("/adminUserCount")
    public long getUsers(){
        return adminUserService.getUserCounts();
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
}
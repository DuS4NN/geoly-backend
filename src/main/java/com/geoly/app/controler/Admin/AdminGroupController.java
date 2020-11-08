package com.geoly.app.controler.Admin;

import com.geoly.app.config.API;
import com.geoly.app.dao.Response;
import com.geoly.app.models.CustomUserDetails;
import com.geoly.app.services.Admin.AdminGroupService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminGroupController {

    private AdminGroupService adminGroupService;

    public AdminGroupController(AdminGroupService adminGroupService) {
        this.adminGroupService = adminGroupService;
    }


    @PreAuthorize("hasAnyRole('MOD, ADMIN')")
    @GetMapping("/adminGroupDetails")
    public Response getGroupDetails(@RequestParam(name = "id") int id){
        try{
            return adminGroupService.getGroupDetails(id);
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("hasAnyRole('MOD, ADMIN')")
    @GetMapping("/adminGroupKickUser")
    public Response kickUserFromGroup(@RequestParam(name = "groupId") int groupId, @RequestParam(name = "userId") int userId, Authentication authentication){
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

            return adminGroupService.kickUserFromGroup(groupId, userId, customUserDetails.getUser().getId());
        }catch (Exception e){
            return API.catchException(e);
        }
    }
}

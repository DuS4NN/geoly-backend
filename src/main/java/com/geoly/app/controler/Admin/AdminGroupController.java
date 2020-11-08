package com.geoly.app.controler.Admin;

import com.geoly.app.config.API;
import com.geoly.app.dao.Response;
import com.geoly.app.services.Admin.AdminGroupService;
import org.springframework.security.access.prepost.PreAuthorize;
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

}

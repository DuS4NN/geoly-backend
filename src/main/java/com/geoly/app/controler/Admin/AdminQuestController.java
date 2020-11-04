package com.geoly.app.controler.Admin;

import com.geoly.app.config.API;
import com.geoly.app.dao.Response;
import com.geoly.app.services.Admin.AdminQuestService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminQuestController {

    private AdminQuestService adminQuestService;

    public AdminQuestController(AdminQuestService adminQuestService) {
        this.adminQuestService = adminQuestService;
    }

    @PreAuthorize("hasAnyRole('MOD, ADMIN')")
    @GetMapping("/adminQuest")
    public Response getQuests(@RequestParam(name = "name") String name, @RequestParam(name = "page") int page){
        try{
            return adminQuestService.getQuests(name, page);
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("hasAnyRole('MOD, ADMIN')")
    @GetMapping("/adminQuestCount")
    public long getUsers(){
        return adminQuestService.getQuestCount();
    }
}
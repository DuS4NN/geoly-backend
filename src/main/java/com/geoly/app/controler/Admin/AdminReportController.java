package com.geoly.app.controler.Admin;

import com.geoly.app.config.API;
import com.geoly.app.dao.Response;
import com.geoly.app.models.CustomUserDetails;
import com.geoly.app.services.Admin.AdminReportService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminReportController {

    private AdminReportService adminReportService;

    public AdminReportController(AdminReportService adminReportService) {
        this.adminReportService = adminReportService;
    }

    @PreAuthorize("hasAnyRole('MOD, ADMIN')")
    @GetMapping("/adminReportUser")
    public Response getUserReports(@RequestParam(name = "nick") String nick, @RequestParam(name = "page") int page){
        try{
            return adminReportService.getUserReports(nick, page);
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("hasAnyRole('MOD, ADMIN')")
    @GetMapping("/adminReportUserDetails")
    public Response getUserReportDetails(@RequestParam(name = "id") int id){
        try{
            return adminReportService.getUserReportDetails(id);
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("hasAnyRole('MOD, ADMIN')")
    @GetMapping("/adminReportUserSolve")
    public Response solveUserReport(@RequestParam(name = "id") int id, Authentication authentication){
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

            return adminReportService.solveUserReport(id, customUserDetails.getUser().getId());
        }catch (Exception e){
            return API.catchException(e);
        }
    }


    @PreAuthorize("hasAnyRole('MOD, ADMIN')")
    @GetMapping("/adminReportQuest")
    public Response getQuestReports(@RequestParam(name = "name") String name, @RequestParam(name = "page") int page){
        try{
            return adminReportService.getQuestReports(name, page);
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("hasAnyRole('MOD, ADMIN')")
    @GetMapping("/adminReportQuestDetails")
    public Response getQuestReportDetails(@RequestParam(name = "id") int id){
        try{
            return adminReportService.getQuestReportDetails(id);
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("hasAnyRole('MOD, ADMIN')")
    @GetMapping("/adminReportQuestSolve")
    public Response solveQuestReport(@RequestParam(name = "id") int id, Authentication authentication){
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

            return adminReportService.solveQuestReport(id, customUserDetails.getUser().getId());
        }catch (Exception e){
            return API.catchException(e);
        }
    }
}

package com.geoly.app.controler.Admin;

import com.geoly.app.config.API;
import com.geoly.app.dao.Response;
import com.geoly.app.services.Admin.AdminReportService;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public Response getReports(@RequestParam(name = "nick") String nick, @RequestParam(name = "page") int page){
        try{
            return adminReportService.getReports(nick, page);
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("hasAnyRole('MOD, ADMIN')")
    @GetMapping("/adminReportUserDetails")
    public Response getReportDetails(@RequestParam(name = "id") int id){
        try{
            return adminReportService.getReportDetails(id);
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("hasAnyRole('MOD, ADMIN')")
    @GetMapping("/adminReportUserSolve")
    public Response solveReport(@RequestParam(name = "id") int id){
        try{
            return adminReportService.solveReport(id);
        }catch (Exception e){
            return API.catchException(e);
        }
    }
}

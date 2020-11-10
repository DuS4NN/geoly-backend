package com.geoly.app.controler.Admin;

import com.geoly.app.config.API;
import com.geoly.app.dao.Response;
import com.geoly.app.services.Admin.AdminSeasonService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminSeasonController {

    private AdminSeasonService adminSeasonService;

    public AdminSeasonController(AdminSeasonService adminSeasonService) {
        this.adminSeasonService = adminSeasonService;
    }

    @PreAuthorize("hasAnyRole('MOD, ADMIN')")
    @GetMapping("/adminSeason")
    public Response getSeason(@RequestParam(name = "page") int page){
        try{
            return adminSeasonService.getSeason(page);
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("hasAnyRole('MOD, ADMIN')")
    @GetMapping("/adminSeasonCount")
    public int getSeasonCount(){
        return adminSeasonService.getSeasonCount();
    }

    @PreAuthorize("hasAnyRole('MOD, ADMIN')")
    @GetMapping("/adminSeasonDetails")
    public Response getSeasonDetails(@RequestParam(name = "page") int page, @RequestParam(name = "year") int year, @RequestParam(name = "month") int month){
        try{
            return adminSeasonService.getSeasonDetails(page, year, month);
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("hasAnyRole('MOD, ADMIN')")
    @GetMapping("/adminSeasonDetailsCount")
    public int getSeasonDetailsCount(@RequestParam(name = "year") int year, @RequestParam(name = "month") int month){
        return adminSeasonService.getSeasonDetailsCount(year, month);
    }
}

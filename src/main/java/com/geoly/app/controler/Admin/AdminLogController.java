package com.geoly.app.controler.Admin;

import com.geoly.app.config.API;
import com.geoly.app.dao.Response;
import com.geoly.app.models.LogType;
import com.geoly.app.services.Admin.AdminLogService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminLogController {

    private AdminLogService adminLogService;

    public AdminLogController(AdminLogService adminLogService) {
        this.adminLogService = adminLogService;
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/adminLog")
    public Response getLogs(@RequestParam(name = "type") LogType logType, @RequestParam(name = "page") int page){
        try{
            return adminLogService.getLogs(logType, page);
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/adminLogTypes")
    public LogType[] getLogTypes(){
        return LogType.values();
    }
}

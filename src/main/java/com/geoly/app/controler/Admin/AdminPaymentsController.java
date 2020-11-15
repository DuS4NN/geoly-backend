package com.geoly.app.controler.Admin;

import com.geoly.app.config.API;
import com.geoly.app.dao.Response;
import com.geoly.app.services.Admin.AdminPaymentsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminPaymentsController {

    private AdminPaymentsService adminPaymentsService;

    public AdminPaymentsController(AdminPaymentsService adminPaymentsService) {
        this.adminPaymentsService = adminPaymentsService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/adminPayment")
    public Response getPayments(@RequestParam(name = "nick") String nick, @RequestParam(name = "page") int page){

        try{
            return adminPaymentsService.getPayments(nick, page);
        }catch (Exception e){
            return API.catchException(e);
        }
    }
}

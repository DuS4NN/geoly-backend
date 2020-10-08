package com.geoly.app.controler;

import com.geoly.app.config.API;
import com.geoly.app.dao.Response;
import com.geoly.app.models.CustomUserDetails;
import com.geoly.app.services.NotificationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

    private NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/notification/count")
    public int getUnseenNotificationCount(Authentication authentication){
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        return notificationService.getCountOfUnseen(customUserDetails.getUser().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/notification")
    public Response getNotifications(@RequestParam(name = "page") int count, Authentication authentication){
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return notificationService.getNotifications(customUserDetails.getUser().getId(), count);
        }catch (Exception e){
            return API.catchException(e);
        }
    }
}

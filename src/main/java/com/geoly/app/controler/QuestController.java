package com.geoly.app.controler;

import com.geoly.app.config.GeolyAPI;
import com.geoly.app.models.CustomUserDetails;
import com.geoly.app.services.QuestService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class QuestController {

    private QuestService questService;

    public QuestController(QuestService questService) {
        this.questService = questService;
    }

    //@PreAuthorize("isAuthenticated()")
    @PostMapping("/signondaily")
    public List signInDailyQuest(Authentication authentication){
        try{
            //CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return questService.signInDailyQuest(1);
        }catch (Exception e){
            return GeolyAPI.catchException(e);
        }
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/dailyquest")
    public List getDailyQuest(Authentication authentication){
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return questService.getDailyQuest(customUserDetails.getUser().getId());
        }catch (Exception e){
            return GeolyAPI.catchException(e);
        }
    }
}

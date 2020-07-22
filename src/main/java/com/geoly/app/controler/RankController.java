package com.geoly.app.controler;

import com.geoly.app.config.GeolyAPI;
import com.geoly.app.models.CustomUserDetails;
import com.geoly.app.models.StatusMessage;
import com.geoly.app.services.RankService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class RankController {

    private RankService rankService;

    public RankController(RankService rankService){
        this.rankService = rankService;
    }

    @GetMapping("/rank")
    public List getTopPlayers(Authentication authentication){
        try{
            List<List> rank = new ArrayList<>();
            rank.add(rankService.getTopPlayers());

            if(authentication != null){
                CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
                rank.add(rankService.getPlayer(customUserDetails.getUser().getId()));
            }else{
                rank.add(Collections.singletonList(new ResponseEntity<>(StatusMessage.USER_NOT_LOGGED_IN, HttpStatus.BAD_REQUEST)));
            }
            return rank;
        }catch (Exception e){
            return GeolyAPI.catchException(e);
        }
    }
}

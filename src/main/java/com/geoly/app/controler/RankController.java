package com.geoly.app.controler;

import com.geoly.app.config.API;
import com.geoly.app.dao.Response;
import com.geoly.app.models.CustomUserDetails;
import com.geoly.app.models.StatusMessage;
import com.geoly.app.services.RankService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RankController {

    private RankService rankService;

    public RankController(RankService rankService){
        this.rankService = rankService;
    }

    @GetMapping("/top")
    public Response getTopPlayers(){
        try{
            return rankService.getTopPlayers();
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @GetMapping("/playerrank")
    public Response getPlayerRank(Authentication authentication){
        try{
            if(authentication != null){
                CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
                return rankService.getPlayer(customUserDetails.getUser().getId());
            }else{
                return new Response(StatusMessage.USER_NOT_LOGGED_IN, HttpStatus.NOT_ACCEPTABLE, null);
            }
        }catch (Exception e){
            return API.catchException(e);
        }
    }
}

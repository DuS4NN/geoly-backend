package com.geoly.app.controler;

import com.geoly.app.config.API;
import com.geoly.app.dao.Response;
import com.geoly.app.models.CustomUserDetails;
import com.geoly.app.models.Stage;
import com.geoly.app.models.User;
import com.geoly.app.repositories.StageRepository;
import com.geoly.app.repositories.UserRepository;
import com.geoly.app.services.GameService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class GameController {

    private GameService gameService;
    private StageRepository stageRepository;
    private UserRepository userRepository;

    public GameController(GameService gameService, StageRepository stageRepository, UserRepository userRepository) {
        this.gameService = gameService;
        this.stageRepository = stageRepository;
        this.userRepository = userRepository;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getUnfinishedStagesClassic")
    public Response getUnfinishedStagesClassic(@RequestParam(name = "questId") int questId, Authentication authentication){
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return gameService.getUnfinishedStagesClassic(questId, customUserDetails.getUser().getId());
        }catch (Exception e){
            return API.catchException(e);
        }
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getUnfinishedStagesParty")
    public Response getUnfinishedStagesParty(@RequestParam(name = "questId") int questId, @RequestParam(name = "partyId") int partyId, Authentication authentication){
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return gameService.getUnfinishedStagesParty(questId, customUserDetails.getUser().getId(), partyId);
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getAdvise")
    public Response getAdvise(@RequestParam(name = "stageId") int stageId, Authentication authentication){
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return gameService.getAdvise(stageId, customUserDetails.getUser().getId());
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/addWrongAnswer")
    public Response addWrongAnswer(@RequestParam(name = "stageId") int stageId, Authentication authentication){
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return gameService.addWrongAnswer(stageId, customUserDetails.getUser().getId());
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/finishStageAndStartNew")
    public Response finishStageAndStartNew(@RequestParam(name = "stageId") int stageId, @RequestParam(name = "questId") int questId, @RequestParam(name = "type") String type, Authentication authentication){
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

            if(type.equals("PARTY")){
                return gameService.finishStageAndStartNewInParty(stageId, questId, customUserDetails.getUser().getId());
            }else{
                return gameService.finishStageAndStartNewInClassic(stageId, questId, customUserDetails.getUser().getId());
            }
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/finishQuest")
    public Response finishQuest(@RequestParam(name = "stageId") int stageId, @RequestParam(name = "type") String type, @RequestParam(name = "questId") int questId, Authentication authentication){
        try{
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

            if(type.equals("PARTY")){
                return gameService.finishQuestInParty(stageId, customUserDetails.getUser().getId());
            }else{
                return gameService.finishQuestInClassic(stageId, customUserDetails.getUser().getId(), questId, type);
            }
        }catch (Exception e){
            return API.catchException(e);
        }
    }
}
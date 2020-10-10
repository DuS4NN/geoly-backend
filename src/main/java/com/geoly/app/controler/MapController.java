package com.geoly.app.controler;

import com.geoly.app.config.API;
import com.geoly.app.dao.Response;
import com.geoly.app.dao.QuestSearch;
import com.geoly.app.models.Category;
import com.geoly.app.models.CustomUserDetails;
import com.geoly.app.models.StageType;
import com.geoly.app.models.StatusMessage;
import com.geoly.app.services.MapService;
import com.geoly.app.validators.Validator;
import com.geoly.app.validators.ValidatorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
public class MapController {

    private MapService mapService;
    private Validator validator;

    public MapController(MapService mapService, Validator validator){
            this.mapService = mapService;
            this.validator = validator;
    }

    @GetMapping(path = "/getCenter")
    public Response getCenterCoordinates(Authentication authentication){
        try{
            if(authentication != null){
                CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
                return mapService.getCenterCoordinates(customUserDetails.getUser().getId());
            }
            return new Response(StatusMessage.USER_NOT_LOGGED_IN, HttpStatus.METHOD_NOT_ALLOWED, null);
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @GetMapping(path = "/questDetail")
    public Response getQuestDetailsById(@RequestParam(name = "id") int id){
        ValidatorResponse validatorResponse = validator.checkOnlyId(id);
        if(!validatorResponse.isValid()) return new Response(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus(), null);

        try{
            return mapService.getQuestDetailsById(id);
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @PostMapping(path = "/questByParam")
    public Response getAllQuestByParametersInRadius(@RequestBody QuestSearch questSearch){
        ValidatorResponse validatorResponse = validator.getAllQuestByParametersInRadius(questSearch);
        if (!validatorResponse.isValid()) return new Response(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus(), null);

        try{
            return mapService.getAllQuestByParametersInRadius(questSearch);
        }catch (Exception e){
            return API.catchException(e);
        }
    }

    @GetMapping(path = "/categories")
    public List<Category> getAllCategories(){
        return mapService.getAllCategories();
    }


    @GetMapping(path = "/stagetypes")
    public List<StageType> getAllStageTypes() {
        return Arrays.asList(StageType.values());
    }
}

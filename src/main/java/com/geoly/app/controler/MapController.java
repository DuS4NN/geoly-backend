package com.geoly.app.controler;

import com.geoly.app.config.API;
import com.geoly.app.config.GeolyAPI;
import com.geoly.app.dao.Response;
import com.geoly.app.dao.questSearch;
import com.geoly.app.models.Category;
import com.geoly.app.services.MapService;
import com.geoly.app.validators.Validator;
import com.geoly.app.validators.ValidatorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
public class MapController {

    private MapService mapService;
    private Validator validator;

    public MapController(MapService mapService, Validator validator){
            this.mapService = mapService;
            this.validator = validator;
    }

    @GetMapping(path = "/{id}")
    public List getQuestDetailsById(@PathVariable(name = "id") Integer id){
        ValidatorResponse validatorResponse = validator.checkOnlyId(id);
        if(!validatorResponse.isValid()) return Collections.singletonList(new ResponseEntity<>(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus()));

        try{
            return mapService.getQuestDetailsById(id);
        }catch (Exception e){
            return GeolyAPI.catchException(e);
        }
    }

    @PostMapping(path = "/questByParam")
    public Response getAllQuestByParametersInRadius(@RequestBody questSearch questSearch){
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
}

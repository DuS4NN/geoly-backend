package com.geoly.app.controler;

import com.geoly.app.config.GeolyAPI;
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

    @GetMapping(path = "/")
    public List getAllQuestByParameters(@RequestParam(required = false, name = "categoryId") List<Integer> categoryId, @RequestParam(required = false, name = "difficulty") List<Integer> difficulty, @RequestParam(required = false, name = "review") List<Integer> review, @RequestParam(required = false, name = "unreviewed") boolean unreviewed){
        ValidatorResponse validatorResponse = validator.getAllQuestByParametersValidator(difficulty, review);
        if (!validatorResponse.isValid()) return Collections.singletonList(new ResponseEntity<>(validatorResponse.getStatusMessage(), validatorResponse.getHttpStatus()));

        try{
            return mapService.getAllQuestsByParameters(categoryId, difficulty, review, unreviewed);
        }catch (Exception e){
            return GeolyAPI.catchException(e);
        }
    }

    @GetMapping(path = "/categories")
    public List<Category> getAllCategories(){
        return mapService.getAllCategories();
    }
}
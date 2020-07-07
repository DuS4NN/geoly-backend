package com.geoly.app.controler;

import com.geoly.app.services.MapService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MapController {

    private MapService mapService;

    public MapController(MapService mapService){
        this.mapService = mapService;
    }

    @GetMapping(path = "/{id}")
    public List getQuestDetailsById(@PathVariable int id){
        return mapService.getQuestDetailsById(id);
    }



}

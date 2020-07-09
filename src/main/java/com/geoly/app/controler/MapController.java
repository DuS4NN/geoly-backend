package com.geoly.app.controler;

import com.geoly.app.services.MapService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MapController {

    private MapService mapService;

    public MapController(MapService mapService){
        this.mapService = mapService;
    }

    @GetMapping(path = "/{id}")
    public List getQuestDetailsById(@PathVariable("id") Integer id){
        return mapService.getQuestDetailsById(id);
    }

    @GetMapping(path = "/")
    public List getAllQuestByParameters(@RequestParam(required = false, name = "categoryId") List<Integer> categoryId, @RequestParam(required = false, name = "difficulty") List<Integer> difficulty, @RequestParam(required = false, name = "review") List<Integer> review, @RequestParam(required = false, name = "unreviewed") boolean unreviewed){
        return mapService.getAllQuestsByParameters(categoryId, difficulty, review, unreviewed);
    }
}

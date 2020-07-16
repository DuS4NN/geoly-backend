package com.geoly.app.controler;

import com.geoly.app.models.Category;
import com.geoly.app.services.MapService;
import io.sentry.Sentry;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MapController {

    private MapService mapService;

        public MapController(MapService mapService){
            this.mapService = mapService;
    }

    @GetMapping(path = "/{id}")
    public List getQuestDetailsById(@PathVariable(name = "id") Integer id) throws Exception {
        try{
            return mapService.getQuestDetailsById(id);
        }catch (Exception e){
            Sentry.capture(e);
            throw  new Exception(e.getMessage());
        }
    }

    @GetMapping(path = "/")
    public List getAllQuestByParameters(@RequestParam(required = false, name = "categoryId") List<Integer> categoryId, @RequestParam(required = false, name = "difficulty") List<Integer> difficulty, @RequestParam(required = false, name = "review") List<Integer> review, @RequestParam(required = false, name = "unreviewed") boolean unreviewed) throws Exception {
        try{
            return mapService.getAllQuestsByParameters(categoryId, difficulty, review, unreviewed);
        }catch (Exception e){
            Sentry.capture(e);
            throw  new Exception(e.getMessage());
        }
    }

    @GetMapping(path = "/categories")
    public List<Category> getAllCategories(){
        return mapService.getAllCategories();
    }
}

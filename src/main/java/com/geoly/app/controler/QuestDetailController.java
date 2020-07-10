package com.geoly.app.controler;

import com.geoly.app.services.QuestDetailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class QuestDetailController {

    private QuestDetailService questDetailService;

    public QuestDetailController(QuestDetailService questDetailService){
        this.questDetailService = questDetailService;
    }

    @GetMapping(path = "quest/{id}")
    public List getDetailsOfQuest(@PathVariable(name = "id") int id){

        List<List> questDetails = new ArrayList<>();

        questDetails.add(questDetailService.getDetailsOfQuest(id));

        if(questDetails.isEmpty()) return null;

        questDetails.add(questDetailService.getReviewsOfQuest(id));
        questDetails.add(questDetailService.getStagesOfQuest(id));
        questDetails.add(questDetailService.getImagesOfQuest(id));

        return questDetails;
    }



}

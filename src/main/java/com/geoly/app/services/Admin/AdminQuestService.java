package com.geoly.app.services.Admin;

import com.geoly.app.config.API;
import com.geoly.app.dao.Response;
import com.geoly.app.jooq.tables.Quest;
import com.geoly.app.models.StatusMessage;
import com.geoly.app.repositories.QuestRepository;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Select;
import org.jooq.impl.DSL;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Service
public class AdminQuestService {

    private EntityManager entityManager;
    private DSLContext create;
    private QuestRepository questRepository;

    public AdminQuestService(EntityManager entityManager, DSLContext create, QuestRepository questRepository) {
        this.entityManager = entityManager;
        this.create = create;
        this.questRepository = questRepository;
    }

    public Response getQuests(String name, int page){

        Condition condition = DSL.trueCondition();
        if(!name.equals("")){
            condition = condition.and(Quest.QUEST.NAME.like("%"+name+"%"));
        }

        Select<?> query =
            create.select(Quest.QUEST.NAME, Quest.QUEST.ID, Quest.QUEST.CREATED_AT)
                .from(Quest.QUEST)
                .where(condition)
                .orderBy(Quest.QUEST.CREATED_AT.desc())
                .limit(20)
                .offset((page - 1)*20);

        Query q = entityManager.createNativeQuery(query.getSQL());
        API.setBindParameterValues(q, query);
        List response = q.getResultList();

        return new Response(StatusMessage.OK, HttpStatus.OK, response);
    }

    public long getQuestCount(){
        return questRepository.count();
    }
}

package com.geoly.app.services.Admin;

import com.geoly.app.config.API;
import com.geoly.app.dao.Response;
import com.geoly.app.jooq.tables.Premium;
import com.geoly.app.jooq.tables.User;
import com.geoly.app.models.StatusMessage;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Select;
import org.jooq.impl.DSL;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminPaymentsService {

    private EntityManager entityManager;
    private DSLContext create;

    public AdminPaymentsService(EntityManager entityManager, DSLContext create) {
        this.entityManager = entityManager;
        this.create = create;
    }

    public Response getPayments(String nick, int page){

        Condition condition = DSL.trueCondition();

        if(!nick.equals("")){
            condition = condition.and(User.USER.NICK_NAME.like("%"+nick+"%"));
        }

        Select<?> count =
                create.select(DSL.count())
                        .from(Premium.PREMIUM)
                        .leftJoin(User.USER)
                        .on(User.USER.ID.eq(Premium.PREMIUM.USER_ID))
                        .where(condition);

        Query q1 = entityManager.createNativeQuery(count.getSQL());
        API.setBindParameterValues(q1, count);
        Object countResult = q1.getSingleResult();

        Select<?> query =
            create.select(User.USER.NICK_NAME, User.USER.PROFILE_IMAGE_URL, Premium.PREMIUM.START_AT, Premium.PREMIUM.END_AT, Premium.PREMIUM.AGREEMENT_ID, Premium.PREMIUM.STATE, Premium.PREMIUM.USER_ID)
                .from(Premium.PREMIUM)
                .leftJoin(User.USER)
                    .on(User.USER.ID.eq(Premium.PREMIUM.USER_ID))
                .where(condition)
                .orderBy(User.USER.NICK_NAME, Premium.PREMIUM.ID.desc())
                .limit(20)
                .offset((page-1)*20);

        Query q = entityManager.createNativeQuery(query.getSQL());
        API.setBindParameterValues(q, query);
        List queryResult = q.getResultList();

        List result = new ArrayList();
        result.add(Integer.parseInt(String.valueOf(countResult)));
        result.add(queryResult);

        return new Response(StatusMessage.OK, HttpStatus.OK, result);
    }
}

package com.geoly.app.services;

import com.geoly.app.config.GeolyAPI;
import com.geoly.app.dao.Response;
import com.geoly.app.jooq.tables.Point;
import com.geoly.app.jooq.tables.User;
import com.geoly.app.models.Badge;
import com.geoly.app.models.BadgeType;
import com.geoly.app.models.StatusMessage;
import com.geoly.app.models.UserBadge;
import com.geoly.app.repositories.BadgeRepository;
import com.geoly.app.repositories.UserRepository;
import io.sentry.Sentry;
import org.jooq.DSLContext;
import org.jooq.Select;
import org.jooq.Table;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.jooq.impl.DSL.*;

@Service
public class RankService {

    private EntityManager entityManager;
    private DSLContext create;
    private BadgeRepository badgeRepository;
    private UserRepository userRepository;

    public RankService(EntityManager entityManager, DSLContext create, BadgeRepository badgeRepository, UserRepository userRepository){
        this.entityManager = entityManager;
        this.create = create;
        this.badgeRepository = badgeRepository;
        this.userRepository = userRepository;
    }

    public Response getTopPlayers(){
        Select<?> query =
            create.select(sum(Point.POINT.AMOUNT), User.USER.NICK_NAME, User.USER.PROFILE_IMAGE_URL)
            .from(Point.POINT)
            .leftJoin(User.USER)
                .on(User.USER.ID.eq(Point.POINT.USER_ID))
            .where(month(Point.POINT.CREATED_AT).eq(month(currentDate())))
            .and(year(Point.POINT.CREATED_AT).eq(year(currentDate())))
            .groupBy(Point.POINT.USER_ID)
            .orderBy(sum(Point.POINT.AMOUNT).desc())
            .limit(50);

        Query q = entityManager.createNativeQuery(query.getSQL());
        GeolyAPI.setBindParameterValues(q, query);
        List result = q.getResultList();

        if(result.isEmpty()) return new Response(StatusMessage.TOP_PLAYERS_NOT_FOUND, HttpStatus.NO_CONTENT, null);
        return new Response(StatusMessage.OK, HttpStatus.OK, result);
    }

    public Response getPlayer(int userId){
        Table<?> rank =
            create.select(Point.POINT.USER_ID.as("userId"))
            .from(Point.POINT)
            .where(month(Point.POINT.CREATED_AT).eq(month(currentDate())))
            .and(year(Point.POINT.CREATED_AT).eq(year(currentDate())))
            .groupBy(Point.POINT.USER_ID)
            .orderBy(sum(Point.POINT.AMOUNT).desc())
            .asTable("rank");

        Table<?> user =
            create.select(sum(Point.POINT.AMOUNT).as("sum"),User.USER.PROFILE_IMAGE_URL.as("profile_img"), User.USER.NICK_NAME.as("nick_name"))
            .from(Point.POINT)
            .leftJoin(User.USER)
                .on(User.USER.ID.eq(Point.POINT.USER_ID))
            .where(month(Point.POINT.CREATED_AT).eq(month(currentDate())))
            .and(year(Point.POINT.CREATED_AT).eq(year(currentDate())))
            .and(Point.POINT.USER_ID.eq(userId))
            .groupBy(Point.POINT.USER_ID)
            .asTable("user");

        Select<?> query =
            create.select(count(), user.field("sum"), user.field("nick_name"), user.field("profile_img"))
            .from(rank, user)
            .where(field("userId").greaterOrEqual(userId));

        Query q = entityManager.createNativeQuery(query.getSQL());
        GeolyAPI.setBindParameterValues(q, query);
        List result = q.getResultList();

        if(result.isEmpty()){
            return new Response(StatusMessage.USER_NOT_IN_TOP, HttpStatus.NO_CONTENT, null);
        }

        Object[] obj = (Object[]) result.get(0);
        if(Integer.parseInt(String.valueOf(obj[0])) <= 2){
            return new Response(StatusMessage.USER_ALREADY_IN_TOP, HttpStatus.NOT_ACCEPTABLE, null);
        }

        return new Response(StatusMessage.OK, HttpStatus.OK, result);
    }

    @Transactional(rollbackOn = Exception.class)
    @Scheduled(cron = "0 0 0 1 1/1 *")
    public void createUserBadge(){
        try{
            Badge badge_1st = badgeRepository.getBadgeByName(BadgeType.FIRST_IN_SEASON.name());
            Badge badge_2nd = badgeRepository.getBadgeByName(BadgeType.SECOND_IN_SEASON.name());
            Badge badge_3rd = badgeRepository.getBadgeByName(BadgeType.THIRD_IN_SEASON.name());
            Badge badge_10top = badgeRepository.getBadgeByName(BadgeType.TOP_10_IN_SEASON.name());
            Badge badge_50top = badgeRepository.getBadgeByName(BadgeType.TOP_50_IN_SEASON.name());

            LocalDate lastMonth = LocalDate.now().minusMonths(1);

            Select<?> query =
                    create.select(Point.POINT.USER_ID)
                            .from(Point.POINT)
                            .where(month(Point.POINT.CREATED_AT).eq(month(lastMonth)))
                            .groupBy(Point.POINT.USER_ID)
                            .orderBy(sum(Point.POINT.AMOUNT).desc())
                            .limit(50);

            Query q = entityManager.createNativeQuery(query.getSQL());
            GeolyAPI.setBindParameterValues(q, query);
            List result = q.getResultList();

            for(int i = 0; i < result.size(); i++){
                int id = Integer.parseInt(String.valueOf(result.get(i)));

                Optional<com.geoly.app.models.User> user = userRepository.findById(id);
                if(!user.isPresent()){
                    Sentry.capture("User not found in RankService/createUserBadge");
                    continue;
                }

                UserBadge userBadge = new UserBadge();
                userBadge.setUser(user.get());
                //O jedno menej pretože indexujem od 0
                if(i > 9){
                    userBadge.setBadge(badge_50top);
                }
                else if(i > 2){
                    userBadge.setBadge(badge_10top);
                }
                else if(i==2){
                    userBadge.setBadge(badge_3rd);
                }
                else if(i==1){
                    userBadge.setBadge(badge_2nd);
                }
                else{
                    userBadge.setBadge(badge_1st);
                }
                entityManager.persist(userBadge);
            }
        }catch (Exception e){
            GeolyAPI.catchException(e);
        }
    }
}

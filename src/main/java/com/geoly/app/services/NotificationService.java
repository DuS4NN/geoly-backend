package com.geoly.app.services;

import com.geoly.app.config.API;
import com.geoly.app.config.GeolyAPI;
import com.geoly.app.dao.Response;
import com.geoly.app.jooq.tables.PartyInvite;
import com.geoly.app.models.Notification;
import com.geoly.app.models.NotificationType;
import com.geoly.app.models.StatusMessage;
import com.geoly.app.models.User;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.pusher.rest.Pusher;
import org.jooq.DSLContext;
import org.jooq.Select;
import org.jooq.Table;
import org.jooq.Update;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.field;

@Service
public class NotificationService {

    private EntityManager entityManager;
    private DSLContext create;
    private Pusher pusher;

    public NotificationService(EntityManager entityManager, DSLContext create, Pusher pusher) {
        this.entityManager = entityManager;
        this.create = create;
        this.pusher = pusher;
    }

    @Async
    @Transactional(rollbackOn = Exception.class)
    public void sendNotification(User user, NotificationType notificationType, HashMap data, boolean push){
        Gson gson = new Gson();
        Notification notification = new Notification();
        notification.setData(gson.toJson(data));
        notification.setSeen(false);
        notification.setUser(user);
        notification.setNotificationType(notificationType);
        entityManager.persist(notification);

        data.put("notificationId", notification.getId());

        if(push){
            String id = Hashing.sha256().hashString(user.getId()+"", StandardCharsets.UTF_8).toString();
            pusher.trigger("notifications-"+id, notificationType.name(), data);
        }
    }

    public Response getCountOfUnseen(int userId){
        Table<?> invite =
            create.select(count().as("inviteCount"))
            .from(PartyInvite.PARTY_INVITE)
            .where(PartyInvite.PARTY_INVITE.USER_ID.eq(userId))
            .and(PartyInvite.PARTY_INVITE.SEEN.isFalse())
            .asTable("invite");

        Table<?> notification =
            create.select(count().as("notificationCount"))
            .from(com.geoly.app.jooq.tables.Notification.NOTIFICATION)
            .where(com.geoly.app.jooq.tables.Notification.NOTIFICATION.SEEN.isFalse())
            .and(com.geoly.app.jooq.tables.Notification.NOTIFICATION.USER_ID.eq(userId))
            .asTable("notification");

        Select<?> query =
            create.select()
            .from(invite, notification);

        Query q = entityManager.createNativeQuery(query.getSQL());
        API.setBindParameterValues(q, query);
        List result = q.getResultList();

        result.add(Hashing.sha256().hashString(userId+"", StandardCharsets.UTF_8).toString());

        return new Response(StatusMessage.OK, HttpStatus.OK, result);
    }

    public void setUnseen(int userId){
        create.update(com.geoly.app.jooq.tables.Notification.NOTIFICATION).set(field("seen"), 1)
                .where(com.geoly.app.jooq.tables.Notification.NOTIFICATION.SEEN.isFalse())
                .and(com.geoly.app.jooq.tables.Notification.NOTIFICATION.USER_ID.eq(userId))
                .execute();
    }

    public Response getNotifications(int userId, int count){
        Select<?> query =
            create.select(com.geoly.app.jooq.tables.Notification.NOTIFICATION.ID, com.geoly.app.jooq.tables.Notification.NOTIFICATION.CREATED_AT, com.geoly.app.jooq.tables.Notification.NOTIFICATION.DATA, com.geoly.app.jooq.tables.Notification.NOTIFICATION.TYPE)
                .from(com.geoly.app.jooq.tables.Notification.NOTIFICATION)
                .where(com.geoly.app.jooq.tables.Notification.NOTIFICATION.USER_ID.eq(userId))
                .orderBy(com.geoly.app.jooq.tables.Notification.NOTIFICATION.CREATED_AT.desc())
                .limit(10)
                .offset(count);

        Query q = entityManager.createNativeQuery(query.getSQL());
        API.setBindParameterValues(q, query);
        List result = q.getResultList();
        return new Response(StatusMessage.OK, HttpStatus.OK, result);
    }
}

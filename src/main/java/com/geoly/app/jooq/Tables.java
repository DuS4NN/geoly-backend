/*
 * This file is generated by jOOQ.
 */
package com.geoly.app.jooq;


import com.geoly.app.jooq.tables.Badge;
import com.geoly.app.jooq.tables.Category;
import com.geoly.app.jooq.tables.Image;
import com.geoly.app.jooq.tables.Language;
import com.geoly.app.jooq.tables.Log;
import com.geoly.app.jooq.tables.Notification;
import com.geoly.app.jooq.tables.Party;
import com.geoly.app.jooq.tables.PartyInvite;
import com.geoly.app.jooq.tables.PartyQuest;
import com.geoly.app.jooq.tables.PartyUser;
import com.geoly.app.jooq.tables.Point;
import com.geoly.app.jooq.tables.Premium;
import com.geoly.app.jooq.tables.Quest;
import com.geoly.app.jooq.tables.QuestReport;
import com.geoly.app.jooq.tables.QuestReview;
import com.geoly.app.jooq.tables.Role;
import com.geoly.app.jooq.tables.Stage;
import com.geoly.app.jooq.tables.Token;
import com.geoly.app.jooq.tables.User;
import com.geoly.app.jooq.tables.UserBadge;
import com.geoly.app.jooq.tables.UserOption;
import com.geoly.app.jooq.tables.UserPartyQuest;
import com.geoly.app.jooq.tables.UserQuest;
import com.geoly.app.jooq.tables.UserReport;
import com.geoly.app.jooq.tables.UserRole;

import javax.annotation.Generated;


/**
 * Convenience access to all tables in geoly
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.5"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Tables {

    /**
     * The table <code>geoly.BADGE</code>.
     */
    public static final Badge BADGE = com.geoly.app.jooq.tables.Badge.BADGE;

    /**
     * The table <code>geoly.CATEGORY</code>.
     */
    public static final Category CATEGORY = com.geoly.app.jooq.tables.Category.CATEGORY;

    /**
     * The table <code>geoly.IMAGE</code>.
     */
    public static final Image IMAGE = com.geoly.app.jooq.tables.Image.IMAGE;

    /**
     * The table <code>geoly.LANGUAGE</code>.
     */
    public static final Language LANGUAGE = com.geoly.app.jooq.tables.Language.LANGUAGE;

    /**
     * The table <code>geoly.LOG</code>.
     */
    public static final Log LOG = com.geoly.app.jooq.tables.Log.LOG;

    /**
     * The table <code>geoly.NOTIFICATION</code>.
     */
    public static final Notification NOTIFICATION = com.geoly.app.jooq.tables.Notification.NOTIFICATION;

    /**
     * The table <code>geoly.PARTY</code>.
     */
    public static final Party PARTY = com.geoly.app.jooq.tables.Party.PARTY;

    /**
     * The table <code>geoly.PARTY_INVITE</code>.
     */
    public static final PartyInvite PARTY_INVITE = com.geoly.app.jooq.tables.PartyInvite.PARTY_INVITE;

    /**
     * The table <code>geoly.PARTY_QUEST</code>.
     */
    public static final PartyQuest PARTY_QUEST = com.geoly.app.jooq.tables.PartyQuest.PARTY_QUEST;

    /**
     * The table <code>geoly.PARTY_USER</code>.
     */
    public static final PartyUser PARTY_USER = com.geoly.app.jooq.tables.PartyUser.PARTY_USER;

    /**
     * The table <code>geoly.POINT</code>.
     */
    public static final Point POINT = com.geoly.app.jooq.tables.Point.POINT;

    /**
     * The table <code>geoly.PREMIUM</code>.
     */
    public static final Premium PREMIUM = com.geoly.app.jooq.tables.Premium.PREMIUM;

    /**
     * The table <code>geoly.QUEST</code>.
     */
    public static final Quest QUEST = com.geoly.app.jooq.tables.Quest.QUEST;

    /**
     * The table <code>geoly.QUEST_REPORT</code>.
     */
    public static final QuestReport QUEST_REPORT = com.geoly.app.jooq.tables.QuestReport.QUEST_REPORT;

    /**
     * The table <code>geoly.QUEST_REVIEW</code>.
     */
    public static final QuestReview QUEST_REVIEW = com.geoly.app.jooq.tables.QuestReview.QUEST_REVIEW;

    /**
     * The table <code>geoly.ROLE</code>.
     */
    public static final Role ROLE = com.geoly.app.jooq.tables.Role.ROLE;

    /**
     * The table <code>geoly.STAGE</code>.
     */
    public static final Stage STAGE = com.geoly.app.jooq.tables.Stage.STAGE;

    /**
     * The table <code>geoly.TOKEN</code>.
     */
    public static final Token TOKEN = com.geoly.app.jooq.tables.Token.TOKEN;

    /**
     * The table <code>geoly.USER</code>.
     */
    public static final User USER = com.geoly.app.jooq.tables.User.USER;

    /**
     * The table <code>geoly.USER_BADGE</code>.
     */
    public static final UserBadge USER_BADGE = com.geoly.app.jooq.tables.UserBadge.USER_BADGE;

    /**
     * The table <code>geoly.USER_OPTION</code>.
     */
    public static final UserOption USER_OPTION = com.geoly.app.jooq.tables.UserOption.USER_OPTION;

    /**
     * The table <code>geoly.USER_PARTY_QUEST</code>.
     */
    public static final UserPartyQuest USER_PARTY_QUEST = com.geoly.app.jooq.tables.UserPartyQuest.USER_PARTY_QUEST;

    /**
     * The table <code>geoly.USER_QUEST</code>.
     */
    public static final UserQuest USER_QUEST = com.geoly.app.jooq.tables.UserQuest.USER_QUEST;

    /**
     * The table <code>geoly.USER_REPORT</code>.
     */
    public static final UserReport USER_REPORT = com.geoly.app.jooq.tables.UserReport.USER_REPORT;

    /**
     * The table <code>geoly.USER_ROLE</code>.
     */
    public static final UserRole USER_ROLE = com.geoly.app.jooq.tables.UserRole.USER_ROLE;
}

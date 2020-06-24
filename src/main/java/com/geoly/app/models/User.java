package com.geoly.app.models;

import com.sun.istack.NotNull;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "user")
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private int id;

    @Column(name = "nick_name", columnDefinition = "VARCHAR(15)")
    @NotNull
    private String nickName;

    @Column(name = "email", columnDefinition = "VARCHAR(254)")
    @NotNull
    private String email;

    @Column(name = "password", columnDefinition = "VARCHAR(60)")
    @NotNull
    private String password;

    @Column(name = "profile_image_url", columnDefinition = "VARCHAR(100)")
    @ColumnDefault("'/static/image/default_profile_image.png'")
    @NotNull
    private String profileImageUrl;

    @Column(name = "about", nullable = false, columnDefinition = "VARCHAR(500)")
    private String about;

    @Column(name = "active", columnDefinition = "TINYINT")
    @ColumnDefault("1")
    @NotNull
    private boolean active;

    @Column(name = "verified", columnDefinition = "TINYINT")
    @ColumnDefault("0")
    @NotNull
    private boolean verified;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    @NotNull
    private String createdAt;

    @Column(name = "address", columnDefinition = "VARCHAR(30)")
    private String address;

    @OneToMany(targetEntity = Quest.class, fetch = FetchType.LAZY, mappedBy = "user")
    private Set<Quest> quest;

    @OneToMany(targetEntity = UserBadge.class, fetch = FetchType.LAZY, mappedBy = "user")
    private Set<UserBadge> userBadge;

    @OneToMany(targetEntity = Token.class, fetch = FetchType.LAZY, mappedBy = "user")
    private Set<Token> token;

    @OneToMany(targetEntity = UserOption.class, fetch = FetchType.LAZY, mappedBy = "user")
    private Set<UserOption> userOption;

    @OneToMany(targetEntity = Premium.class, fetch = FetchType.LAZY, mappedBy = "user")
    private Set<Premium> premium;

    @OneToMany(targetEntity = Transaction.class, fetch = FetchType.LAZY, mappedBy = "user")
    private Set<Transaction> transaction;

    @OneToMany(targetEntity = Point.class, fetch = FetchType.LAZY, mappedBy = "user")
    private Set<Point> point;

    @OneToMany(targetEntity = UserRole.class, fetch = FetchType.LAZY, mappedBy = "user")
    private Set<UserRole> userRole;

    @OneToMany(targetEntity = Party.class, fetch = FetchType.LAZY, mappedBy = "user")
    private Set<Party> party;

    @OneToMany(targetEntity = PartyUser.class, fetch = FetchType.LAZY, mappedBy = "user")
    private Set<PartyUser> partyUser;

    @OneToMany(targetEntity = QuestReport.class, fetch = FetchType.LAZY, mappedBy = "user")
    private Set<QuestReport> questReport;

    @OneToMany(targetEntity = QuestReview.class, fetch = FetchType.LAZY, mappedBy = "user")
    private Set<QuestReview> questReview;

    @OneToMany(targetEntity = UserQuest.class, fetch = FetchType.LAZY, mappedBy = "user")
    private Set<UserQuest> userQuest;

    @OneToMany(targetEntity = UserReport.class, fetch = FetchType.LAZY, mappedBy = "userReported")
    private Set<UserReport> userReported;

    @OneToMany(targetEntity = UserReport.class, fetch = FetchType.LAZY, mappedBy = "userComplainant")
    private Set<UserReport> userComplainant;

    public User() {
    }
}

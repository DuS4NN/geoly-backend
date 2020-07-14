package com.geoly.app.models;

import com.sun.istack.NotNull;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "user")
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private int id;

    @Column(name = "nick_name", columnDefinition = "VARCHAR(15)", unique = true)
    @NotNull
    private String nickName;

    @Column(name = "email", columnDefinition = "VARCHAR(254)", unique = true)
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
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "address", columnDefinition = "VARCHAR(30)")
    private String address;

    @ManyToMany
    @JoinTable(name = "user_role")
    private Set<Role> role;

    @OneToMany(targetEntity = Quest.class, fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Quest> quest;

    @OneToMany(targetEntity = UserBadge.class, fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private Set<UserBadge> userBadge;

    @OneToMany(targetEntity = Token.class, fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Token> token;

    @OneToMany(targetEntity = UserOption.class, fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private Set<UserOption> userOption;

    @OneToMany(targetEntity = Premium.class, fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Premium> premium;

    @OneToMany(targetEntity = Transaction.class, fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Transaction> transaction;

    @OneToMany(targetEntity = Point.class, fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Point> point;

    @OneToMany(targetEntity = Party.class, fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Party> party;

    @OneToMany(targetEntity = PartyUser.class, fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private Set<PartyUser> partyUser;

    @OneToMany(targetEntity = QuestReport.class, fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private Set<QuestReport> questReport;

    @OneToMany(targetEntity = QuestReview.class, fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private Set<QuestReview> questReview;

    @OneToMany(targetEntity = UserQuest.class, fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private Set<UserQuest> userQuest;

    @OneToMany(targetEntity = UserReport.class, fetch = FetchType.LAZY, mappedBy = "userReported", cascade = CascadeType.ALL)
    private Set<UserReport> userReported;

    @OneToMany(targetEntity = UserReport.class, fetch = FetchType.LAZY, mappedBy = "userComplainant", cascade = CascadeType.ALL)
    private Set<UserReport> userComplainant;

    @OneToMany(targetEntity = PartyInvite.class, fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private Set<PartyInvite> partyInvite;

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<Quest> getQuest() {
        return quest;
    }

    public void setQuest(Set<Quest> quest) {
        this.quest = quest;
    }

    public Set<UserBadge> getUserBadge() {
        return userBadge;
    }

    public void setUserBadge(Set<UserBadge> userBadge) {
        this.userBadge = userBadge;
    }

    public Set<Token> getToken() {
        return token;
    }

    public void setToken(Set<Token> token) {
        this.token = token;
    }

    public Set<UserOption> getUserOption() {
        return userOption;
    }

    public void setUserOption(Set<UserOption> userOption) {
        this.userOption = userOption;
    }

    public Set<Premium> getPremium() {
        return premium;
    }

    public void setPremium(Set<Premium> premium) {
        this.premium = premium;
    }

    public Set<Transaction> getTransaction() {
        return transaction;
    }

    public void setTransaction(Set<Transaction> transaction) {
        this.transaction = transaction;
    }

    public Set<Point> getPoint() {
        return point;
    }

    public void setPoint(Set<Point> point) {
        this.point = point;
    }

    public Set<Party> getParty() {
        return party;
    }

    public void setParty(Set<Party> party) {
        this.party = party;
    }

    public Set<PartyUser> getPartyUser() {
        return partyUser;
    }

    public void setPartyUser(Set<PartyUser> partyUser) {
        this.partyUser = partyUser;
    }

    public Set<QuestReport> getQuestReport() {
        return questReport;
    }

    public void setQuestReport(Set<QuestReport> questReport) {
        this.questReport = questReport;
    }

    public Set<QuestReview> getQuestReview() {
        return questReview;
    }

    public void setQuestReview(Set<QuestReview> questReview) {
        this.questReview = questReview;
    }

    public Set<UserQuest> getUserQuest() {
        return userQuest;
    }

    public void setUserQuest(Set<UserQuest> userQuest) {
        this.userQuest = userQuest;
    }

    public Set<UserReport> getUserReported() {
        return userReported;
    }

    public void setUserReported(Set<UserReport> userReported) {
        this.userReported = userReported;
    }

    public Set<UserReport> getUserComplainant() {
        return userComplainant;
    }

    public void setUserComplainant(Set<UserReport> userComplainant) {
        this.userComplainant = userComplainant;
    }

    public Set<PartyInvite> getPartyInvite() {
        return partyInvite;
    }

    public void setPartyInvite(Set<PartyInvite> partyInvite) {
        this.partyInvite = partyInvite;
    }

    public Set<Role> getRole() {
        return role;
    }

    public void setRole(Set<Role> role) {
        this.role = role;
    }
}

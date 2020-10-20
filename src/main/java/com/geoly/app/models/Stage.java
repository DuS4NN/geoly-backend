package com.geoly.app.models;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "stage")
public class Stage {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private int id;

    @ManyToOne(targetEntity = Quest.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "quest_id")
    @NotNull
    private Quest quest;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    @NotNull
    private StageType type;

    @Column(name = "latitude", columnDefinition = "DOUBLE")
    private Double latitude;

    @Column(name = "longitude", columnDefinition = "DOUBLE")
    private Double longitude;

    @Column(name = "question", columnDefinition = "VARCHAR(200)")
    private String question;

    @Column(name = "answer", columnDefinition = "VARCHAR(200)")
    private String answer;

    @Column(name = "qr_code_url", columnDefinition = "VARCHAR(100)")
    private String qrCodeUrl;

    @Column(name = "answers_list", columnDefinition = "VARCHAR(1000)")
    private String answersList;

    @Column(name = "note", columnDefinition = "VARCHAR(200)")
    private String note;

    @Column(name = "advise", columnDefinition = "VARCHAR(200)")
    private String advise;

    @OneToMany(targetEntity = UserQuest.class, fetch = FetchType.LAZY, mappedBy = "stage", cascade = CascadeType.ALL)
    private Set<UserQuest> userQuest;

    @OneToMany(targetEntity = UserPartyQuest.class, fetch = FetchType.LAZY, mappedBy = "stage", cascade = CascadeType.ALL)
    private Set<UserPartyQuest> userPartyQuest;

    public Stage() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Quest getQuest() {
        return quest;
    }

    public void setQuest(Quest quest) {
        this.quest = quest;
    }

    public StageType getType() {
        return type;
    }

    public void setType(StageType type) {
        this.type = type;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Set<UserQuest> getUserQuest() {
        return userQuest;
    }

    public void setUserQuest(Set<UserQuest> userQuest) {
        this.userQuest = userQuest;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAdvise() {
        return advise;
    }

    public void setAdvise(String advise) {
        this.advise = advise;
    }

    public String getAnswersList() {
        return answersList;
    }

    public void setAnswersList(String answersList) {
        this.answersList = answersList;
    }

    public Set<UserPartyQuest> getUserPartyQuest() {
        return userPartyQuest;
    }

    public void setUserPartyQuest(Set<UserPartyQuest> userPartyQuest) {
        this.userPartyQuest = userPartyQuest;
    }
}

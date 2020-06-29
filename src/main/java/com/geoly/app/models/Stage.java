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

    @ManyToOne(targetEntity = Type.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    @NotNull
    private Type type;

    @Column(name = "latiude", columnDefinition = "VARCHAR(10)")
    private String latiude;

    @Column(name = "longitude", columnDefinition = "VARCHAR(10)")
    private String longitude;

    @Column(name = "question", columnDefinition = "VARCHAR(200)")
    private String question;

    @Column(name = "answer", columnDefinition = "VARCHAR(200)")
    private String answer;

    @OneToMany(targetEntity = UserQuest.class, fetch = FetchType.LAZY, mappedBy = "stage", cascade = CascadeType.ALL)
    private Set<UserQuest> userQuest;

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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getLatiude() {
        return latiude;
    }

    public void setLatiude(String latiude) {
        this.latiude = latiude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
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
}

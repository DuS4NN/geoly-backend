package com.geoly.app.models;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_quest")
public class UserQuest {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private int id;

    @ManyToOne(targetEntity = Quest.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "quest_id")
    @NotNull
    private Quest quest;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @ManyToOne(targetEntity = Stage.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "stage_id")
    @NotNull
    private Stage stage;

    @ManyToOne(targetEntity = Status.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    @NotNull
    private Status status;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    @NotNull
    private Date createdAt;

    public UserQuest() {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}

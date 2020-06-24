package com.geoly.app.models;

import com.sun.istack.NotNull;

import javax.persistence.*;

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
    private String createdAt;

    public UserQuest() {
    }
}

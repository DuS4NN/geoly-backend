package com.geoly.app.models;

import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "quest_review")
public class QuestReview {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private int id;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "Id")
    @NotNull
    private User user;

    @ManyToOne(targetEntity = Quest.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "quest_id", referencedColumnName = "Id")
    @NotNull
    private Quest quest;

    @Column(name = "review_text", columnDefinition = "VARCHAR(500)")
    @NotNull
    private String reviewText;

    @Column(name = "review", columnDefinition = "TINYINT")
    @NotNull
    private int review;

    public QuestReview() {
    }
}

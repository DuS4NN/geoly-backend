package com.geoly.app.models;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "quest")
public class Quest {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private int id;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @ManyToOne(targetEntity = Category.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @NotNull
    private Category category;

    @Column(name = "difficulty", columnDefinition = "TINYINT")
    @NotNull
    private int difficulty;

    @Column(name = "description", nullable = false, columnDefinition = "VARCHAR(500)")
    private String decription;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    @NotNull
    private String createdAt;

    @Column(name = "active", columnDefinition = "TINYINT")
    @NotNull
    private boolean active;

    @Column(name = "private_quest", columnDefinition = "TINYINT")
    @NotNull
    private boolean privateQuest;

    @Column(name = "daily", columnDefinition = "TINYINT")
    @NotNull
    private boolean daily;

    @OneToMany(targetEntity = Stage.class, fetch = FetchType.LAZY, mappedBy = "quest", cascade = CascadeType.ALL)
    private Set<Stage> stage;

    @OneToMany(targetEntity = Image.class, fetch = FetchType.LAZY, mappedBy = "quest", cascade = CascadeType.ALL)
    private Set<Image> image;

    @OneToMany(targetEntity = PartyQuest.class, fetch = FetchType.LAZY, mappedBy = "quest", cascade = CascadeType.ALL)
    private Set<PartyQuest> partyQuest;

    @OneToMany(targetEntity = QuestReport.class, fetch = FetchType.LAZY, mappedBy = "quest", cascade = CascadeType.ALL)
    private Set<QuestReport> questReport;

    @OneToMany(targetEntity = QuestReview.class, fetch = FetchType.LAZY, mappedBy = "quest", cascade = CascadeType.ALL)
    private Set<QuestReview> questReview;

    @OneToMany(targetEntity = UserQuest.class, fetch = FetchType.LAZY, mappedBy = "quest", cascade = CascadeType.ALL)
    private Set<UserQuest> userQuest;

    public Quest() {
    }
}

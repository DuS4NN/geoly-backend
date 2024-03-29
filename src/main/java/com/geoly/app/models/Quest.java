package com.geoly.app.models;

import com.sun.istack.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "QUEST")
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

    @Column(name = "premium", columnDefinition = "TINYINT")
    @NotNull
    private boolean premium;

    @Column(name = "description", nullable = false, columnDefinition = "VARCHAR(500)")
    private String description;

    @Column(name = "name", nullable = false, columnDefinition = "VARCHAR(50)")
    private String name;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    @CreationTimestamp
    private Date createdAt;

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

    public Quest() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public boolean isPrivateQuest() {
        return privateQuest;
    }

    public void setPrivateQuest(boolean privateQuest) {
        this.privateQuest = privateQuest;
    }

    public boolean isDaily() {
        return daily;
    }

    public void setDaily(boolean daily) {
        this.daily = daily;
    }

    public Set<Stage> getStage() {
        return stage;
    }

    public void setStage(Set<Stage> stage) {
        this.stage = stage;
    }

    public Set<Image> getImage() {
        return image;
    }

    public void setImage(Set<Image> image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<PartyQuest> getPartyQuest() {
        return partyQuest;
    }

    public void setPartyQuest(Set<PartyQuest> partyQuest) {
        this.partyQuest = partyQuest;
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
}

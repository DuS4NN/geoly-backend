package com.geoly.app.models;

import com.sun.istack.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_party_quest")
public class UserPartyQuest {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private int id;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @ManyToOne(targetEntity = Stage.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "stage_id")
    @NotNull
    private Stage stage;

    @ManyToOne(targetEntity = PartyQuest.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "party_quest_id")
    @NotNull
    private PartyQuest partyQuest;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @NotNull
    private UserQuestStatus status;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP")
    @UpdateTimestamp
    private Date updatedAt;

    public UserPartyQuest() {
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

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public PartyQuest getPartyQuest() {
        return partyQuest;
    }

    public void setPartyQuest(PartyQuest partyQuest) {
        this.partyQuest = partyQuest;
    }

    public UserQuestStatus getStatus() {
        return status;
    }

    public void setStatus(UserQuestStatus status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}

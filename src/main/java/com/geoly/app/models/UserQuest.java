package com.geoly.app.models;

import com.sun.istack.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "USER_QUEST")
public class UserQuest {

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

    @Column(name = "advise_used", columnDefinition = "TINYINT")
    @NotNull
    private boolean adviseUsed;

    @Column(name = "wrong_answers", columnDefinition = "INT")
    @NotNull
    private int wrongAnswers;

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

    public UserQuest() {
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

    public boolean isAdviseUsed() {
        return adviseUsed;
    }

    public void setAdviseUsed(boolean adviseUsed) {
        this.adviseUsed = adviseUsed;
    }

    public int getWrongAnswers() {
        return wrongAnswers;
    }

    public void setWrongAnswers(int wrongAnswers) {
        this.wrongAnswers = wrongAnswers;
    }
}

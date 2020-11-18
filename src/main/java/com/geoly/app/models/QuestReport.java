package com.geoly.app.models;

import com.sun.istack.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "QUEST_REPORT")
public class QuestReport {

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

    @Enumerated(EnumType.STRING)
    @Column(name = "reason")
    @NotNull
    private QuestReportReason reason;

    @Column(name = "solved", columnDefinition = "TINYINT")
    @NotNull
    private boolean solved;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    @CreationTimestamp
    private Date createdAt;

    public QuestReport() {
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
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

    public Quest getQuest() {
        return quest;
    }

    public void setQuest(Quest quest) {
        this.quest = quest;
    }

    public QuestReportReason getReason() {
        return reason;
    }

    public void setReason(QuestReportReason reason) {
        this.reason = reason;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}

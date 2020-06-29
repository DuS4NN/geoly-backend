package com.geoly.app.models;

import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "user_badge")
public class UserBadge {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private int id;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @ManyToOne(targetEntity = Badge.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "badge_id")
    @NotNull
    private Badge badge;

    @Column(name = "received_at", columnDefinition = "TIMESTAMP")
    @NotNull
    private String receivedAt;

    public UserBadge() {
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

    public Badge getBadge() {
        return badge;
    }

    public void setBadge(Badge badge) {
        this.badge = badge;
    }

    public String getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(String receivedAt) {
        this.receivedAt = receivedAt;
    }
}

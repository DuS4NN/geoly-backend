package com.geoly.app.models;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_report")
public class UserReport {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private int id;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "reported")
    @NotNull
    private User userReported;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "complainant")
    @NotNull
    private User userComplainant;

    @Column(name = "reason", columnDefinition = "VARCHAR(20)")
    @NotNull
    private String reason;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    @NotNull
    private Date createdAt;

    public UserReport() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUserReported() {
        return userReported;
    }

    public void setUserReported(User userReported) {
        this.userReported = userReported;
    }

    public User getUserComplainant() {
        return userComplainant;
    }

    public void setUserComplainant(User userComplainant) {
        this.userComplainant = userComplainant;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}

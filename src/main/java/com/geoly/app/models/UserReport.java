package com.geoly.app.models;

import com.sun.istack.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(
    name = "user_report",
    uniqueConstraints = @UniqueConstraint(columnNames = {"reported", "complainant"}))
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

    @Enumerated(EnumType.STRING)
    @Column(name = "reason")
    @NotNull
    private UserReportReason reason;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    @CreationTimestamp
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

    public UserReportReason getReason() {
        return reason;
    }

    public void setReason(UserReportReason reason) {
        this.reason = reason;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}

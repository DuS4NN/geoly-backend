package com.geoly.app.models;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private int id;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @Column(name = "uuid", columnDefinition = "VARCHAR(100)")
    @NotNull
    private String uuid;

    @Column(name = "amount", columnDefinition = "FLOAT")
    @NotNull
    private float amount;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    @NotNull
    private Date createdAt;

    @Column(name = "verified", columnDefinition = "TINYINT")
    @NotNull
    private boolean verified;

    @OneToMany(targetEntity = Premium.class, fetch = FetchType.LAZY, mappedBy = "transaction", cascade = CascadeType.ALL)
    private Set<Premium> premium;

    public Transaction() {
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public Set<Premium> getPremium() {
        return premium;
    }

    public void setPremium(Set<Premium> premium) {
        this.premium = premium;
    }
}

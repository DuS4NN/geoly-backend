package com.geoly.app.models;

import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "point")
public class Point {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private int id;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @Column(name = "amount", columnDefinition = "INT")
    @NotNull
    private int amount;

    @Column(name = "receive_at", columnDefinition = "TIMESTAMP")
    @NotNull
    private String receiveAt;

    public Point() {
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getReceiveAt() {
        return receiveAt;
    }

    public void setReceiveAt(String receiveAt) {
        this.receiveAt = receiveAt;
    }
}

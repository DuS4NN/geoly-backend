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
}

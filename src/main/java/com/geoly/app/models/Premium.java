package com.geoly.app.models;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "premium")
public class Premium {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private int id;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @ManyToOne(targetEntity = Transaction.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id")
    @NotNull
    private Transaction transaction;

    @Column(name = "start_at", columnDefinition = "TIMESTAMP")
    @NotNull
    private String startAt;

    @Column(name = "end_at", columnDefinition = "TIMESTAMP")
    @NotNull
    private String endAt;


    public Premium() {
    }
}

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

    @Column(name = "start_at", columnDefinition = "TIMESTAMP")
    @NotNull
    private String startAt;

    @Column(name = "end_at", columnDefinition = "TIMESTAMP")
    @NotNull
    private String endAt;

    @OneToMany(targetEntity = Transaction.class, fetch = FetchType.LAZY, mappedBy = "premium")
    private Set<Transaction> transaction;

    public Premium() {
    }
}

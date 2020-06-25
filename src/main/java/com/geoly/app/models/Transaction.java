package com.geoly.app.models;

import com.sun.istack.NotNull;

import javax.persistence.*;
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

    @Column(name = "amount", columnDefinition = "INT")
    @NotNull
    private int amount;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    @NotNull
    private String createdAt;

    @Column(name = "verified", columnDefinition = "TINYINT")
    @NotNull
    private boolean verified;

    @OneToMany(targetEntity = Premium.class, fetch = FetchType.LAZY, mappedBy = "transaction", cascade = CascadeType.ALL)
    private Set<Premium> premium;

    public Transaction() {
    }
}

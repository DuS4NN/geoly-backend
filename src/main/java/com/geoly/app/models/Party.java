package com.geoly.app.models;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "party")
public class Party {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private int id;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @Column(name = "name", columnDefinition = "VARCHAR(15)")
    @NotNull
    private String name;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    @NotNull
    private Date createdAt;

    @OneToMany(targetEntity = PartyUser.class, fetch = FetchType.LAZY, mappedBy = "party", cascade = CascadeType.ALL)
    private Set<PartyUser> partyUser;

    @OneToMany(targetEntity = PartyQuest.class, fetch = FetchType.LAZY, mappedBy = "party", cascade = CascadeType.ALL)
    private Set<PartyQuest> partyQuest;

    public Party() {
    }
}

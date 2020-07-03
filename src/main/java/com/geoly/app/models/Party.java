package com.geoly.app.models;

import com.sun.istack.NotNull;
import org.hibernate.annotations.CreationTimestamp;

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
    @CreationTimestamp
    private Date createdAt;

    @OneToMany(targetEntity = PartyUser.class, fetch = FetchType.LAZY, mappedBy = "party", cascade = CascadeType.ALL)
    private Set<PartyUser> partyUser;

    @OneToMany(targetEntity = PartyQuest.class, fetch = FetchType.LAZY, mappedBy = "party", cascade = CascadeType.ALL)
    private Set<PartyQuest> partyQuest;

    @OneToMany(targetEntity = PartyInvite.class, fetch = FetchType.LAZY, mappedBy = "party", cascade = CascadeType.ALL)
    private Set<PartyInvite> partyInvite;

    public Party() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Set<PartyUser> getPartyUser() {
        return partyUser;
    }

    public void setPartyUser(Set<PartyUser> partyUser) {
        this.partyUser = partyUser;
    }

    public Set<PartyQuest> getPartyQuest() {
        return partyQuest;
    }

    public void setPartyQuest(Set<PartyQuest> partyQuest) {
        this.partyQuest = partyQuest;
    }

    public Set<PartyInvite> getPartyInvate() {
        return partyInvite;
    }

    public void setPartyInvate(Set<PartyInvite> partyInvate) {
        this.partyInvite = partyInvate;
    }
}

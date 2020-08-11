package com.geoly.app.models;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "party_quest")
public class PartyQuest {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private int id;

    @ManyToOne(targetEntity = Party.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id")
    @NotNull
    private Party party;

    @ManyToOne(targetEntity = Quest.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "quest_id")
    @NotNull
    private Quest quest;

    @Column(name = "active", columnDefinition = "TINYINT")
    @NotNull
    private boolean active;

    @OneToMany(targetEntity = UserPartyQuest.class, fetch = FetchType.LAZY, mappedBy = "partyQuest", cascade = CascadeType.ALL)
    private Set<UserPartyQuest> userPartyQuest;

    public PartyQuest() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public Quest getQuest() {
        return quest;
    }

    public void setQuest(Quest quest) {
        this.quest = quest;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<UserPartyQuest> getUserPartyQuest() {
        return userPartyQuest;
    }

    public void setUserPartyQuest(Set<UserPartyQuest> userPartyQuest) {
        this.userPartyQuest = userPartyQuest;
    }
}

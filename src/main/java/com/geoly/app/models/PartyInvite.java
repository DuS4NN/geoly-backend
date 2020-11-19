package com.geoly.app.models;

import com.sun.istack.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PARTY_INVITE")
public class PartyInvite {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private int id;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @ManyToOne(targetEntity = Party.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id")
    @NotNull
    private Party party;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @NotNull
    private PartyInvateStatus status;

    @Column(name = "seen", columnDefinition = "TINYINT")
    @NotNull
    private boolean seen;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    @CreationTimestamp
    private Date created_at;

    public PartyInvite() {
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
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

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public PartyInvateStatus getStatus() {
        return status;
    }

    public void setStatus(PartyInvateStatus status) {
        this.status = status;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }
}

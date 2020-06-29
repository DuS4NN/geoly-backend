package com.geoly.app.models;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "status")
public class Status {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private int id;

    @Column(name = "name", columnDefinition = "VARCHAR(20)")
    @NotNull
    private String name;

    @OneToMany(targetEntity = UserQuest.class, fetch = FetchType.LAZY, mappedBy = "status", cascade = CascadeType.ALL)
    private Set<UserQuest> userQuest;

    public Status() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<UserQuest> getUserQuest() {
        return userQuest;
    }

    public void setUserQuest(Set<UserQuest> userQuest) {
        this.userQuest = userQuest;
    }
}

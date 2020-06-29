package com.geoly.app.models;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "type")
public class Type {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private int id;

    @Column(name = "name", columnDefinition = "VARCHAR(20)")
    @NotNull
    private String name;

    @OneToMany(targetEntity = Stage.class, fetch = FetchType.LAZY, mappedBy = "type", cascade = CascadeType.ALL)
    private Set<Stage> stage;

    public Type() {
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

    public Set<Stage> getStage() {
        return stage;
    }

    public void setStage(Set<Stage> stage) {
        this.stage = stage;
    }
}


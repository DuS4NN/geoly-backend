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
}


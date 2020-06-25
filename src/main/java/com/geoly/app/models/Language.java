package com.geoly.app.models;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "language")
public class Language {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private int id;

    @Column(name = "name", columnDefinition = "VARCHAR(20)")
    @NotNull
    private String name;

    @Column(name = "image_url", columnDefinition = "VARCHAR(100)")
    @NotNull
    private String imageUrl;

    @OneToMany(targetEntity = UserOption.class, fetch = FetchType.LAZY, mappedBy = "language", cascade = CascadeType.ALL)
    private Set<UserOption> userOption;

    public Language() {
    }
}

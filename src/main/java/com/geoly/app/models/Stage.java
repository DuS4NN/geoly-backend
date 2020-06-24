package com.geoly.app.models;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "stage")
public class Stage {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private int id;

    @ManyToOne(targetEntity = Quest.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "quest_id")
    @NotNull
    private Quest quest;

    @ManyToOne(targetEntity = Type.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    @NotNull
    private Type type;

    @Column(name = "latiude", columnDefinition = "VARCHAR(10)")
    private String latiude;

    @Column(name = "longitude", columnDefinition = "VARCHAR(10)")
    private String longitude;

    @Column(name = "question", columnDefinition = "VARCHAR(200)")
    private String question;

    @Column(name = "answer", columnDefinition = "VARCHAR(200)")
    private String answer;

    @OneToMany(targetEntity = UserQuest.class, fetch = FetchType.LAZY, mappedBy = "stage")
    private Set<UserQuest> userQuest;

    public Stage() {
    }
}

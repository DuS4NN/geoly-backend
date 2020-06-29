package com.geoly.app.models;

import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "image")
public class Image {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private int id;

    @ManyToOne(targetEntity = Quest.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "quest_id")
    @NotNull
    private Quest quest;

    @Column(name = "image_url", columnDefinition = "VARCHAR(100)")
    @NotNull
    private String imageUrl;

    public Image() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Quest getQuest() {
        return quest;
    }

    public void setQuest(Quest quest) {
        this.quest = quest;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

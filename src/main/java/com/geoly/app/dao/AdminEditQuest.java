package com.geoly.app.dao;

public class AdminEditQuest {

    private int id;
    private String name;
    private String description;
    private boolean active;
    private boolean premium;
    private boolean privateQuest;
    private int difficulty;
    private int category;

    public AdminEditQuest() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public boolean isPrivateQuest() {
        return privateQuest;
    }

    public void setPrivateQuest(boolean privateQuest) {
        this.privateQuest = privateQuest;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "id:"+id+",name:"+name+",description:"+description+",active:"+active+",premium:"+premium+",privateQuest:"+privateQuest+",difficulty:"+difficulty+",category:"+category;
    }
}

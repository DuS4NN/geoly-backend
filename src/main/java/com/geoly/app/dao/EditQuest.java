package com.geoly.app.dao;

public class EditQuest {

    private int categoryId;
    private String description;
    private String name;
    private boolean privateQuest;
    private int difficulty;

    public EditQuest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}

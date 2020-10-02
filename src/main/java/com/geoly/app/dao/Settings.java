package com.geoly.app.dao;

public class Settings {

    private String about;
    private int languageId;
    private boolean privateProfile;

    public Settings(String about, int languageId, boolean privateProfile) {
        this.about = about;
        this.languageId = languageId;
        this.privateProfile = privateProfile;
    }

    public Settings() {
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public boolean isPrivateProfile() {
        return privateProfile;
    }

    public void setPrivateProfile(boolean privateProfile) {
        this.privateProfile = privateProfile;
    }
}

package com.geoly.app.dao;

public class AdminEditUser {

    private int id;
    private String nickName;
    private String email;
    private String address;
    private String about;
    private int language;
    private boolean active;
    private boolean verified;
    private boolean privateProfile;


    public AdminEditUser() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public boolean isPrivateProfile() {
        return privateProfile;
    }

    public void setPrivateProfile(boolean privateProfile) {
        this.privateProfile = privateProfile;
    }


    @Override
    public String toString() {
        return "id:"+id+",email:"+email+",nickName:"+nickName+",address:"+address+",about:"+about+",language:"+language+",active:"+active+",verified:"+verified+",private:"+privateProfile;
    }
}

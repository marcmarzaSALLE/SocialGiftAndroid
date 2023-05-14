package com.example.socialgift.controller;

import com.example.socialgift.model.User;

public class UserData {
    private static UserData userData;
    private String name;
    private String last_name;
    private String email;
    private String image;

    private boolean initialized;


    private UserData() {
        initialized = false;
    }

    public static UserData getInstance() {
        if (userData == null) {
            synchronized (UserData.class) {
                if (userData == null) {
                    userData = new UserData();
                }
            }
        }
        return userData;
    }

    private void checkInitialization() {
        if (name != null && email != null && last_name != null && image != null) {
            initialized = true;
        }
    }

    public String getName() {
        return name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    public String getImage() {
        return image;
    }

    public void setName(String name) {
        this.name = name;
        checkInitialization();
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
        checkInitialization();
    }

    public void setEmail(String email) {
        this.email = email;
        checkInitialization();
    }

    public void setImage(String image) {
        this.image = image;
        checkInitialization();
    }

    public boolean isInitialized() {
        return initialized;
    }

}

package com.example.socialgift.model;

public class User {
    private String id;
    private String name;
    private String last_name;
    private String email;
    private String password;
    private String image;

    public User(String name, String last_name, String email, String password,String image) {
        this.name = name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.image=image;
    }

    public String getId() {
        return id;
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

    public String getPassword() {
        return password;
    }

    public String getImage() {
        return image;
    }
}

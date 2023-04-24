package com.example.socialgift.model;

public class User {
    private String id;
    private String name;
    private String last_name;
    private String email;
    private String password;
    private String image;

    public User(String name, String last_name, String email, String password) {
        this.name = name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
    }
}

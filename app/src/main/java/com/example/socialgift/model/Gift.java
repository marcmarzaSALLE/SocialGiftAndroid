package com.example.socialgift.model;

import java.io.Serializable;

public class Gift implements Serializable {
    private int id;
    private String name;
    private String description;
    private String link;
    private String urlImage;
    private double price;
    private boolean isAdded;
    private int idCategory;

    public Gift(int id, String name, String description, String link, String urlImage, double price, int idCategory) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.link = link;
        this.urlImage = urlImage;
        this.price = price;
        this.idCategory = idCategory;
    }

    public Gift() {
        this.isAdded = false;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }

    public boolean isAdded() {
        return isAdded;
    }

    public void setAdded(boolean added) {
        isAdded = added;
    }
}

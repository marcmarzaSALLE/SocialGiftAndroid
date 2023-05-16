package com.example.socialgift.model;

import java.io.Serializable;

public class GiftWishList implements Serializable {
    private int id;
    private int idWishList;
    private String productLink;
    private int priority;
    private boolean booked;

    public GiftWishList(int id, int idWishList, String productLink, int priority) {
        this.id = id;
        this.idWishList = idWishList;
        this.productLink = productLink;
        this.priority = priority;
        this.booked = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdWishList() {
        return idWishList;
    }

    public void setIdWishList(int idWishList) {
        this.idWishList = idWishList;
    }

    public String getProductLink() {
        return productLink;
    }

    public void setProductLink(String productLink) {
        this.productLink = productLink;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isBooked() {
        return booked;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }
}

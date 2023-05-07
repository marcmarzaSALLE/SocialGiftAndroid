package com.example.socialgift.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Wishlist implements Serializable {
    private int id;
    private String nameList;
    private String descriptionList;
    private String createdDateList;
    private String endDateList;
    private ArrayList<Gift>gifts;

    public Wishlist(int id, String nameList, String descriptionList, String createdDateList, String endDateList, ArrayList<Gift> gifts) {
        this.id = id;
        this.nameList = nameList;
        this.descriptionList = descriptionList;
        this.createdDateList = createdDateList;
        this.endDateList = endDateList;
        this.gifts = gifts;
    }

    public Wishlist() {
    }

    public int getId() {
        return id;
    }

    public String getNameList() {
        return nameList;
    }

    public String getDescriptionList() {
        return descriptionList;
    }

    public String getCreatedDateList() {
        return createdDateList;
    }

    public String getEndDateList() {
        return endDateList;
    }

    public ArrayList<Gift> getGifts() {
        return gifts;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNameList(String nameList) {
        this.nameList = nameList;
    }

    public void setDescriptionList(String descriptionList) {
        this.descriptionList = descriptionList;
    }

    public void setCreatedDateList(String createdDateList) {
        this.createdDateList = createdDateList;
    }

    public void setEndDateList(String endDateList) {
        this.endDateList = endDateList;
    }

    public void setGifts(ArrayList<Gift> gifts) {
        this.gifts = gifts;
    }
}

package com.example.socialgift.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Wishlist implements Serializable {
    private int id;
    private String nameList;
    private String descriptionList;
    private String createdDateList;
    private String endDateList;

    private int bookedGifts;
    private ArrayList<GiftWishList>gifts;

    public Wishlist(int id, String nameList, String descriptionList, String createdDateList, String endDateList, ArrayList<GiftWishList> gifts) {
        this.id = id;
        this.nameList = nameList;
        this.descriptionList = descriptionList;
        this.createdDateList = createdDateList;
        this.endDateList = endDateList;
        this.gifts = gifts;
    }

    public Wishlist() {
        this.gifts = new ArrayList<>();
        this.bookedGifts = 0;
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

    public ArrayList<GiftWishList> getGifts() {
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

    public void setGifts(ArrayList<GiftWishList> gifts) {
        this.gifts = gifts;
    }

    public int getBookedGifts() {
        return bookedGifts;
    }

    public void setBookedGifts(int bookedGifts) {
        this.bookedGifts = bookedGifts;
    }

    @Override
    public String toString() {
        return "Wishlist{" +
                "id=" + id +
                ", nameList='" + nameList + '\'' +
                ", descriptionList='" + descriptionList + '\'' +
                '}';
    }
}

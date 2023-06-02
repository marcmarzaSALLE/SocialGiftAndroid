package com.example.socialgift.model;

import java.io.Serializable;

public class Message implements Serializable {
    private int id;
    private String content;
    private int user_id_send;
    private int user_id_receive;
    private String timeStamp;

    public Message() {
    }

    public Message(int id, String content, int user_id_send, int user_id_receive, String timeStamp) {
        this.id = id;
        this.content = content;
        this.user_id_send = user_id_send;
        this.user_id_receive = user_id_receive;
        this.timeStamp = timeStamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUser_id_send() {
        return user_id_send;
    }

    public void setUser_id_send(int user_id_send) {
        this.user_id_send = user_id_send;
    }

    public int getUser_id_receive() {
        return user_id_receive;
    }

    public void setUser_id_receive(int user_id_receive) {
        this.user_id_receive = user_id_receive;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}

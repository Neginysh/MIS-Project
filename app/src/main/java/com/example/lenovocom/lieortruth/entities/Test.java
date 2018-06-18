package com.example.lenovocom.lieortruth.entities;

public class Test {
    private int id;
    private String date;
    private Boolean sentToServer;

    public Test() {
    }

    public Test(int id, String date, Boolean sentToServer) {
        this.id = id;
        this.date = date;
        this.sentToServer = sentToServer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Boolean getSentToServer() {
        return sentToServer;
    }

    public void setSentToServer(Boolean sentToServer) {
        this.sentToServer = sentToServer;
    }
}

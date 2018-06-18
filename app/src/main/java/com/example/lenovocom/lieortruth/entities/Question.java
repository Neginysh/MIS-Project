package com.example.lenovocom.lieortruth.entities;

public class Question {
    private int id;
    private String text;

    public Question() {
    }

    public Question(int id, String Text) {
        this.id = id;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }
}

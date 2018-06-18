package com.example.lenovocom.lieortruth.entities;

public class User {
    private int id;
    private String nickName;
    private int age;

    public User (){
    }

    public User(int id, String nickName, int age) {
        this.id = id;
        this.nickName = nickName;
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

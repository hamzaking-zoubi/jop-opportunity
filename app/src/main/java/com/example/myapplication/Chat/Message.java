package com.example.myapplication.Chat;

public class Message {
    private String name;
    private String text;
    private  String photoUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
    public Message(String text, String name) {
        this.name = name;
        this.text = text;

    }
    public Message(){}
    public Message(String text, String name, String photoUrl) {
        this.name = name;
        this.text = text;
        this.photoUrl = photoUrl;
    }
}

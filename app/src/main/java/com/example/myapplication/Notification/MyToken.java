package com.example.myapplication.Notification;

import java.io.Serializable;

public class MyToken implements Serializable {
    private  String myToken;

    public String getMyToken() {
        return myToken;
    }

    public MyToken(String myToken) {
        this.myToken = myToken;
    }

    public MyToken() {
    }

    public void setMyToken(String myToken) {
        this.myToken = myToken;
    }
}

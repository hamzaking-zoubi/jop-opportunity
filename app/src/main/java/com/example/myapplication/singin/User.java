package com.example.myapplication.singin;

import java.io.Serializable;

public class User implements Serializable {
  private   String name;
    private  String Id;
    private  String password;
    private String email;

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User(String name, String id, String password, String email) {
        this.name = name;
        this.Id = id;
        this.password = password;
        this.email = email;
    }
}

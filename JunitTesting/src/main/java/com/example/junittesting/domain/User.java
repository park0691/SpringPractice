package com.example.junittesting.domain;

public class User {
    String id;
    String name;
    String pw;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public User() { }

    public User(String id, String name, String pw) {
        this.id = id;
        this.name = name;
        this.pw = pw;
    }
}

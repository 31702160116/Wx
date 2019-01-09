package com.example.raymond.weixin;

public class Newslnfo {
    private String name;
    private String user;

    public  Newslnfo(String name ,String user){
        this.name=name;
        this.user=user;
    }
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

package com.atguigu.netty.groupChat.entity;


import lombok.Getter;
import lombok.Setter;

public class User {
    @Getter
    @Setter
    private int id;
    @Getter
    @Setter
    private String pwd;

    public User() {
    }

    public User(int id, String pwd) {
        this.id = id;
        this.pwd = pwd;
    }

}

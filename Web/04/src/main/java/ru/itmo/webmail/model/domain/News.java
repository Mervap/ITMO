package ru.itmo.webmail.model.domain;

import java.io.Serializable;

//Task 6
public class News implements Serializable {
    private long userId;
    private String text;

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getUserId() {
        return userId;
    }

    public String getText() {
        return text;
    }
}

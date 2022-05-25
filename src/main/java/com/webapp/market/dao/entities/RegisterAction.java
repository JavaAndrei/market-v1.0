package com.webapp.market.dao.entities;

import java.util.Date;

public class RegisterAction {

    private int id;
    private User user;
    private Date date;
    private String action;

    public RegisterAction() {
    }

    public RegisterAction(User user, Date date, String action) {
        this.user = user;
        this.date = date;
        this.action = action;
    }

    public RegisterAction(int id, User user, Date date, String action) {
        this.id = id;
        this.user = user;
        this.date = date;
        this.action = action;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return String.format("%d %s %s %s", id, user.getLogin(), date, action);
    }

    public String toString(String mode) {
        if (mode.equals("table"))
            return String.format("|%10d |%25s |%25s |%25s |", id, user.getLogin(), date, action);
        return "";
    }
}

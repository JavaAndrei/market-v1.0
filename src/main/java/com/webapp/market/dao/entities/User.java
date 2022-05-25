package com.webapp.market.dao.entities;

import java.io.Serializable;

public class User implements Serializable {

    private int id;
    private String login;
    private String password;
    private Role role;

    public User() {
    }

    public User(int id) {
        this.id = id;
    }

    public User(String login) {
        this.login = login;
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public User(String login, String password, Role role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public User(int id, String login, String password, Role role) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return String.format("%d %s %s %s", id, login, password, role);
    }

    public String toString(String mode) {
        if (mode.equals("table"))
            return String.format("|%10d |%25s |%25s |%15s |", id, login, password, role);
        return "";
    }
}
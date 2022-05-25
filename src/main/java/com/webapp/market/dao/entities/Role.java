package com.webapp.market.dao.entities;

public class Role {

    private int id;
    private String name;

    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }

    public Role(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public String toString(String mode) {
        if (mode.equals("table"))
            return String.format("|%10d |%25s |", id, name);
        return "";
    }
}

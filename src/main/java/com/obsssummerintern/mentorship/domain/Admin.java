package com.obsssummerintern.mentorship.domain;

public class Admin {
    private String uid;
    private String name;
    private String surname;
    private String password;

    public Admin(String uid, String name, String surname, String password) {
        this.uid = uid;
        this.name = name;
        this.surname = surname;
        this.password = password;
    }

    public Admin(String name, String surname, String password) {
        this.name = name;
        this.surname = surname;
        this.password = password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }
}

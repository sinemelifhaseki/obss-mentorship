package com.obsssummerintern.mentorship.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="users")
public class RegularUser implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long user_id;

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    private String uid;

    private String name;
    private String email; // user will add it manually

    private String lastName;


    public RegularUser(String id, String name, String lastName) {
        this.uid = id;
        this.name = name;
    }

    public String getlastName() {
        return lastName;
    }

    public void setlastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;


    public RegularUser(){

    }

    public String getId() {
        return uid;
    }

    public void setId(String  Id) {
        this.uid = Id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

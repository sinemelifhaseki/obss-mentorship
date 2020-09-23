package com.obsssummerintern.mentorship.domain;

import javax.persistence.*;

@Entity
@Table(name="allowed_users")
public class AllowedUser {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    private String name;
    private String uid;
    private String email;

    public AllowedUser() {
    }

    public AllowedUser(String name, String email, String uid) {
        this.name = name;
        this.email = email;
        this.uid = uid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

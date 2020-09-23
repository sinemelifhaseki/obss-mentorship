package com.obsssummerintern.mentorship.domain;

import lombok.Builder;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name ="TOPICS")
public class Topic {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name="title", nullable = false)
    private String title;

    /*
    @OneToMany(mappedBy = "topic",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Subtopic> subtopics; */

    public Topic(){}

    public Topic(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
/*
    public Set<Subtopic> getSubtopics() {
        return subtopics;
    }

    public void setSubtopics(Set<Subtopic> Subtopics) {
        this.subtopics = Subtopics;
    }*/
}

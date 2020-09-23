package com.obsssummerintern.mentorship.domain;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name ="SUBTOPICS")
public class Subtopic implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name="title", nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name="topic_id", referencedColumnName = "id")
    private Topic topic;

    public Subtopic() {}

    // GETTER SETTERS
    public Subtopic(String title, Topic topic) {
        this.title = title;
        this.topic = topic;
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

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }
}

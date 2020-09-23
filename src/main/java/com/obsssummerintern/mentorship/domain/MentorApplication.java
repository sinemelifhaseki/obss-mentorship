package com.obsssummerintern.mentorship.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="applications")
public class MentorApplication implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String details;
    private String email;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="topic_id", referencedColumnName = "id")
    private Topic topic;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="subtopic_id", referencedColumnName = "id")
    private Subtopic subtopic;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="user_id", referencedColumnName = "user_id")
    private RegularUser user;

    private Subtopic[] subtopics;


    public MentorApplication() {
    }

    public MentorApplication(String name, String details,String email, Topic topic, Subtopic subtopic, RegularUser user) {
        this.name = name;
        this.details = details;
        this.topic = topic;
        this.subtopic = subtopic;
        this.email = email;
        this.user = user;
    }

    public MentorApplication(String name, String details,String email, Topic topic, Subtopic subtopic, Subtopic[] subtopics) {
        this.name = name;
        this.details = details;
        this.topic = topic;
        this.subtopic = subtopic;
        this.email = email;
        this.subtopics = subtopics;
    }

    // GETTER SETTERS

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public Subtopic[] getSubtopics() {
        return subtopics;
    }

    public void setSubtopics(Subtopic[] subtopics) {
        this.subtopics = subtopics;
    }

    public Subtopic getSubtopic() {
        return subtopic;
    }

    public void setSubtopic(Subtopic subtopic) {
        this.subtopic = subtopic;
    }

    public RegularUser getUser() {
        return user;
    }

    public void setUser(RegularUser user) {
        this.user = user;
    }
}

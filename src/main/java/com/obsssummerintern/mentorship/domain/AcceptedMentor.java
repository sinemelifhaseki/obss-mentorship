package com.obsssummerintern.mentorship.domain;

import javax.persistence.*;

@Entity
@Table(name="accepted_mentors")
public class AcceptedMentor {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String details;
    private String email;

    @ManyToOne
    @JoinColumn(name="topic_id", referencedColumnName = "id")
    private Topic topic;

    @ManyToOne
    @JoinColumn(name="subtopic_id", referencedColumnName = "id")
    private Subtopic subtopic;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "user_id")
    private RegularUser user;

    private Subtopic[] subtopics;

    // CONSTRUCTORS
    public AcceptedMentor() {
    }

    public AcceptedMentor(String name, String details, String email, Topic topic, Subtopic subtopic, Subtopic[] subtopics, RegularUser regularUser) {
        this.name = name;
        this.details = details;
        this.email = email;
        this.topic = topic;
        this.subtopic = subtopic;
        this.subtopics = subtopics;
        this.user = regularUser;
    }

    public AcceptedMentor(String name, String details, String email, Topic topic, Subtopic subtopic, RegularUser regularUser) {
        this.name = name;
        this.details = details;
        this.email = email;
        this.topic = topic;
        this.subtopic = subtopic;
        this.user = regularUser;
    }
    //GETTER SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RegularUser getUser() {
        return user;
    }

    public void setUser(RegularUser user) {
        this.user = user;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public Subtopic getSubtopic() {
        return subtopic;
    }

    public void setSubtopic(Subtopic subtopic) {
        this.subtopic = subtopic;
    }

    public Subtopic[] getSubtopics() {
        return subtopics;
    }

    public void setSubtopics(Subtopic[] subtopics) {
        this.subtopics = subtopics;
    }
}

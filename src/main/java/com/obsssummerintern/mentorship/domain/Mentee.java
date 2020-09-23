package com.obsssummerintern.mentorship.domain;

import javax.persistence.*;
import javax.swing.text.DateFormatter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// FOR RELATIONSHIP BETWEEN A MENTOR AND MENTEE
@Entity
@Table(name = "mentees")
public class Mentee {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name="name", nullable = false)
    private String name;
    private String email;

    @Column
    private String today;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "user_id")
    private RegularUser user;

    @ManyToOne
    @JoinColumn(name="mentor_id", referencedColumnName = "id")
    private AcceptedMentor acceptedMentor;

    @ManyToOne
    @JoinColumn(name="topic_id", referencedColumnName = "id")
    private Topic topic;

    @ManyToOne
    @JoinColumn(name="subtopic_id", referencedColumnName = "id")
    private Subtopic subtopic;

    private String phaseStatus;

    public Mentee() {
    }

    public Mentee(String name, String email, AcceptedMentor acceptedMentor, Topic topic, Subtopic subtopic, RegularUser user, String phaseStatus) {
        this.name = user.getName();
        this.email = email;
        this.acceptedMentor = acceptedMentor;
        this.topic = topic;
        this.user = user;
        this.subtopic = subtopic;
        this.today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MMM-yy"));
        this.phaseStatus = phaseStatus;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AcceptedMentor getAcceptedMentor() {
        return acceptedMentor;
    }

    public void setAcceptedMentor(AcceptedMentor acceptedMentor) {
        this.acceptedMentor = acceptedMentor;
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

    public String getToday() {
        return today;
    }

    public RegularUser getUser() {
        return user;
    }

    public void setUser(RegularUser user) {
        this.user = user;
    }

    public String getPhaseStatus() {
        return phaseStatus;
    }

    public void setPhaseStatus(String phaseStatus) {
        this.phaseStatus = phaseStatus;
    }
}

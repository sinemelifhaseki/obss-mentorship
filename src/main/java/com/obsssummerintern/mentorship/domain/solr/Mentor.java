package com.obsssummerintern.mentorship.domain.solr;

import com.obsssummerintern.mentorship.domain.AcceptedMentor;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import javax.persistence.Id;

@SolrDocument(collection = "Mentors")
public class Mentor {
    @Id
    private String id;

    @Indexed(name="mentorid",type = "long")
    private Long mentorid;

    @Indexed(name = "mentorname", type = "string")
    private String name;

    @Indexed(name = "details", type = "string")
    private String details;

    @Indexed(name = "email", type = "string")
    private String email;

    @Indexed(name = "topic", type = "string")
    private String topic;

    @Indexed(name = "subtopic", type = "string")
    private String subtopic;

    public Mentor(){}

    public Mentor(AcceptedMentor acceptedMentor){
        this.mentorid=acceptedMentor.getId();
        this.name = acceptedMentor.getName();
        this.details = acceptedMentor.getDetails();
        this.email = acceptedMentor.getEmail();
        this.topic = acceptedMentor.getTopic().getTitle();
        this.subtopic = acceptedMentor.getSubtopic().getTitle();
    }

    public Mentor(Long mentorid, String name, String details, String email, String topic, String subtopic) {
        this.mentorid = mentorid;
        this.name = name;
        this.details = details;
        this.email = email;
        this.topic = topic;
        this.subtopic = subtopic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getMentorid() {
        return mentorid;
    }

    public void setMentorid(Long mentorid) {
        this.mentorid = mentorid;
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

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getSubtopic() {
        return subtopic;
    }

    public void setSubtopic(String subtopic) {
        this.subtopic = subtopic;
    }
}

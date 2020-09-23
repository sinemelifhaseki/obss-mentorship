package com.obsssummerintern.mentorship.domain;

import javax.persistence.*;

@Entity
@Table(name = "phases")
public class Phase {

    @Id
    @GeneratedValue
    private Long id;

    private String endDate;
    private String title;
    private String status; //Active, Finished, Hasn't started
    private String phaseNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="mentee_id", referencedColumnName = "id")
    private Mentee mentee;

    private String mentorRating;
    private String menteeRating;

    private String mentorFeedback;
    private String menteeFeedback;

    //              CONSTRUCTORS                     //
    public Phase(){
    }

    public Phase(String endDate, String title, String status, Mentee mentee, String phaseNumber) {
        this.endDate = endDate;
        this.title = title;
        this.status = status;
        this.mentee = mentee;
        this.phaseNumber = phaseNumber;
    }
    //              GETTER SETTERS                      //
    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Mentee getMentee() {
        return mentee;
    }

    public void setMentee(Mentee mentee) {
        this.mentee = mentee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhaseNumber() {
        return phaseNumber;
    }

    public void setPhaseNumber(String phaseNumber) {
        this.phaseNumber = phaseNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMentorRating() {
        return mentorRating;
    }

    public void setMentorRating(String mentorRating) {
        this.mentorRating = mentorRating;
    }

    public String getMenteeRating() {
        return menteeRating;
    }

    public void setMenteeRating(String menteeRating) {
        this.menteeRating = menteeRating;
    }

    public String getMentorFeedback() {
        return mentorFeedback;
    }

    public void setMentorFeedback(String mentorFeedback) {
        this.mentorFeedback = mentorFeedback;
    }

    public String getMenteeFeedback() {
        return menteeFeedback;
    }

    public void setMenteeFeedback(String menteeFeedback) {
        this.menteeFeedback = menteeFeedback;
    }
}

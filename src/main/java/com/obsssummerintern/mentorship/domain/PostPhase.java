package com.obsssummerintern.mentorship.domain;

import javax.persistence.*;

@Entity
@Table(name = "postphases")
public class PostPhase {
    @Id
    @GeneratedValue
    private Long id;

    private String stars;
    private String feedback;
    private Boolean byMentor; // true if rated by admin, else by mentee

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="phase_id", referencedColumnName = "id")
    private Phase phase;

    public PostPhase() {
    }

    public PostPhase(String stars, String feedback, Phase phase, Boolean byMentor) {
        this.stars = stars;
        this.feedback = feedback;
        this.phase = phase;
        this.byMentor = byMentor;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }

    public String getStars() {
        return stars;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public Boolean getByMentor() {
        return byMentor;
    }

    public void setByMentor(Boolean byMentor) {
        this.byMentor = byMentor;
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }
}

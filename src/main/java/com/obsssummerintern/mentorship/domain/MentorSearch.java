package com.obsssummerintern.mentorship.domain;

public class MentorSearch {

    private Topic topic;
    private Subtopic[] subtopics;
    private String searchtext;

    public MentorSearch() {
    }

    public MentorSearch(Topic topic, Subtopic[] subtopics, String searchtext) {
        this.topic = topic;
        this.subtopics = subtopics;
        this.searchtext = searchtext;
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

    public String getSearchtext() {
        return searchtext;
    }

    public void setSearchtext(String searchtext) {
        this.searchtext = searchtext;
    }
}

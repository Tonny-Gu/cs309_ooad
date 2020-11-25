package com.sustech.dboj.backend.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private User student;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Contest contest;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Question question;

    @Column(nullable = false, columnDefinition="text")
    private String content;

    @Column(nullable = false)
    private String time;

    public Comment() {

    }

    public Integer getId() {
        return Id;
    }

    public void setId( Integer id ) {
        Id = id;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent( User student ) {
        this.student = student;
    }

    public Contest getContest() {
        return contest;
    }

    public void setContest( Contest contest ) {
        this.contest = contest;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion( Question question ) {
        this.question = question;
    }

    public String getContent() {
        return content;
    }

    public void setContent( String content ) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "Id=" + Id +
                ", student=" + student +
                ", contest=" + contest +
                ", question=" + question +
                ", content='" + content + '\'' +
                '}';
    }
}

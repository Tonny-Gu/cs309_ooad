package com.sustech.dboj.backend.domain;

import javax.persistence.*;

@Entity
@Table(name = "submit_log")
public class Submission {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private User student;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Contest contest;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Question question;

    @Column(nullable = false)
    private String submitTime;
    @Column(nullable = false)
    private String info;

    public Submission() {
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

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime( String submitTime ) {
        this.submitTime = submitTime;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo( String info ) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "Submission{" +
                "Id=" + Id +
                ", student=" + student +
                ", contest=" + contest +
                ", question=" + question +
                ", submitTime='" + submitTime + '\'' +
                ", info='" + info + '\'' +
                '}';
    }
}

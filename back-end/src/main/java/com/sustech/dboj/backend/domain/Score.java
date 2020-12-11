package com.sustech.dboj.backend.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;

@Entity
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private User student;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Contest contest;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Question question;

    @Column(nullable = false)
    private Integer submit = 0;

    @Column(nullable = false)
    private Boolean ac = false;

    @Column(nullable = false)
    private Integer wa = 0;

    @Column
    private String acTime;

    public Score() {
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

    public Integer getSubmit() {
        return submit;
    }

    public void setSubmit( Integer submit ) {
        this.submit = submit;
    }

    public Boolean getAc() {
        return ac;
    }

    public void setAc( Boolean ac ) {
        this.ac = ac;
    }

    public Integer getWa() {
        return wa;
    }

    public void setWa( Integer wa ) {
        this.wa = wa;
    }


    public String getAcTime() {
        return acTime;
    }

    public void setAcTime( String acTime ) {
        this.acTime = acTime;
    }

    @Override
    public String toString() {
        return "Score{" +
                "Id=" + Id +
                ", student=" + student +
                ", contest=" + contest +
                ", question=" + question +
                ", submit=" + submit +
                ", ac=" + ac +
                ", wa=" + wa +
                ", acTime='" + acTime + '\'' +
                '}';
    }
}

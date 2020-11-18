package com.sustech.dboj.backend.domain;


import javax.persistence.*;

@Entity
public class TestCase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Question question;

    @Column(nullable = false)
    private String env; // docker id

    @Column(nullable = false)
    private String answerCode; // standard ans

    @Column(nullable = false)
    private Boolean Orderliness;

    public TestCase() {
    }

    public Integer getId() {
        return Id;
    }

    public void setId( Integer id ) {
        Id = id;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion( Question question ) {
        this.question = question;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv( String env ) {
        this.env = env;
    }

    public String getAnswerCode() {
        return answerCode;
    }

    public void setAnswerCode( String answerCode ) {
        this.answerCode = answerCode;
    }

    public Boolean getOrderliness() {
        return Orderliness;
    }

    public void setOrderliness( Boolean orderliness ) {
        Orderliness = orderliness;
    }

    @Override
    public String toString() {
        return "TestCase{" +
                "Id=" + Id +
                ", question=" + question +
                ", env='" + env + '\'' +
                ", answerCode='" + answerCode + '\'' +
                ", Orderliness=" + Orderliness +
                '}';
    }
}

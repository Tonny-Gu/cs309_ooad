package com.sustech.dboj.backend.domain;


import javax.persistence.*;

@Entity
public class TestCase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Question question;

    @Column
    private String env; // docker id

    @Column(nullable = false)
    private String initDB;


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

    public String getInitDB() {
        return initDB;
    }

    public void setInitDB( String initDB ) {
        this.initDB = initDB;
    }

    @Override
    public String toString() {
        return "TestCase{" +
                "Id=" + Id +
                ", question=" + question +
                ", env='" + env + '\'' +
                ", initDB='" + initDB + '\'' +
                '}';
    }
}

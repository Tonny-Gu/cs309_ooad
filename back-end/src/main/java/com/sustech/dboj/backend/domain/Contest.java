package com.sustech.dboj.backend.domain;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Contest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String beginTime;
    @Column(nullable = false)
    private String endTime;
    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)
    private Set<Question> questions;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)
    private Set<User> users;

    public Contest() {
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers( Set<User> users ) {
        this.users = users;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public void setQuestions( Set<Question> questions ) {
        this.questions = questions;
    }

    public Integer getId() {
        return Id;
    }

    public void setId( Integer id ) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime( String beginTime ) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime( String endTime ) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Contest{" +
                "Id=" + Id +
                ", name='" + name + '\'' +
                ", beginTime='" + beginTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", questions=" + questions +
                ", users=" + users +
                '}';
    }
}

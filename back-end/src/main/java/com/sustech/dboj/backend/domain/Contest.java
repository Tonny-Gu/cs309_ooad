package com.sustech.dboj.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    @Column(nullable = false, name = "en_able")
    private Boolean enable;
    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)
    private Set<Question> questions;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)
    @JsonIgnore //for security
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

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable( Boolean enable ) {
        this.enable = enable;
    }

    @Override
    public String toString() {
        return "Contest{" +
                "Id=" + Id +
                ", name='" + name + '\'' +
                ", beginTime='" + beginTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", enable=" + enable +
                ", questions=" + questions +
                ", users=" + users +
                '}';
    }
}

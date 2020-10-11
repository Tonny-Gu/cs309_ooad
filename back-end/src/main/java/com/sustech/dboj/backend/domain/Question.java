package com.sustech.dboj.backend.domain;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    @Column(nullable = false)
    private String name;
    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)
    private Set<Contest> contests;
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "author", nullable = false)
    private User author;
    @Column(nullable = false)
    private String fileAddress;

    public Question() {
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

    public Set<Contest> getContests() {
        return contests;
    }

    public void setLesson( Set<Contest> contest ) {
        this.contests = contest;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor( User author ) {
        this.author = author;
    }

    public String getFileAddress() {
        return fileAddress;
    }

    public void setFileAddress( String fileAddress ) {
        this.fileAddress = fileAddress;
    }

    @Override
    public String toString() {
        return "Question{" +
                "Id=" + Id +
                ", name='" + name + '\'' +
                ", contests=" + contests +
                ", author=" + author +
                ", fileAddress='" + fileAddress + '\'' +
                '}';
    }
}

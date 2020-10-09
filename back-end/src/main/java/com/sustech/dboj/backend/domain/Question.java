package com.sustech.dboj.backend.domain;

import javax.persistence.*;

public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    @Column(nullable = false)
    private String name;
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "lesson", nullable = false)
    private Lesson lesson;
    @ManyToOne(cascade = {CascadeType.ALL})
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

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson( Lesson lesson ) {
        this.lesson = lesson;
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
                ", lesson=" + lesson +
                ", author=" + author +
                ", fileAddress='" + fileAddress + '\'' +
                '}';
    }
}

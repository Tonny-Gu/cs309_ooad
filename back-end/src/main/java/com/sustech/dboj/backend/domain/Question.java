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
    @OneToOne()// 去掉cascade = CascadeType.ALL就成了，取消关联的级联新增！
    @JoinColumn(name = "author", nullable = false)
    private User author;
    @Column(nullable = false, columnDefinition="text")
    private String content;

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

    public String getContent() {
        return content;
    }

    public void setContent( String content ) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Question{" +
                "Id=" + Id +
                ", name='" + name + '\'' +
                ", contests=" + contests +
                ", author=" + author +
                ", content='" + content + '\'' +
                '}';
    }

}

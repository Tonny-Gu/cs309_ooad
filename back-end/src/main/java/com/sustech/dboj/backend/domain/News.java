package com.sustech.dboj.backend.domain;
import javax.persistence.*;

@Entity(name = "notice")
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @Column(nullable = false)
    private String topic;

    @OneToOne()
    @JoinColumn(name = "author", nullable = false)
    private User author;

    @Column(nullable = false, columnDefinition = "text")
    private String content;

    @Column(nullable = false)
    private String time;

    @Column(nullable = false, name = "en_able")
    private Boolean enable;

    public News() {
    }

    public Integer getId() {
        return Id;
    }

    public void setId( Integer id ) {
        Id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic( String topic ) {
        this.topic = topic;
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

    public String getTime() {
        return time;
    }

    public void setTime( String time ) {
        this.time = time;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable( Boolean enable ) {
        this.enable = enable;
    }

    @Override
    public String toString() {
        return "News{" +
                "Id=" + Id +
                ", topic='" + topic + '\'' +
                ", author=" + author +
                ", content='" + content + '\'' +
                ", time='" + time + '\'' +
                ", enable=" + enable +
                '}';
    }
}

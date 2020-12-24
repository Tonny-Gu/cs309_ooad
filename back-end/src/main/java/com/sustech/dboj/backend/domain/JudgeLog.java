package com.sustech.dboj.backend.domain;

import javax.persistence.*;

@Entity
public class JudgeLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @Column(nullable = false, columnDefinition = "text")
    private String info;

    @Column
    private Integer submission;

    public JudgeLog(  ) {
    }

    public Integer getId() {
        return Id;
    }

    public void setId( Integer id ) {
        Id = id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo( String info ) {
        this.info = info;
    }

    public Integer getSubmission() {
        return submission;
    }

    public void setSubmission( Integer submission ) {
        this.submission = submission;
    }

    @Override
    public String toString() {
        return "JudgeLog{" +
                "Id=" + Id +
                ", info='" + info + '\'' +
                '}';
    }
}

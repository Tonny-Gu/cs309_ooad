package com.sustech.dboj.backend.domain;

import javax.persistence.*;

@Entity
public class Comment {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private User student;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Contest contest;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Question question;

    @Column(nullable = false)
    private String fileAddress;
}

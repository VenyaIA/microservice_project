package com.ivanov.microservice_project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project; // Связь с проектом

    @ManyToOne
    private User author;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task; // Связь с задачей

    private String content;
}

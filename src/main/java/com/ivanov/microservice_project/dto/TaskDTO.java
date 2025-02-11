package com.ivanov.microservice_project.dto;

import lombok.Data;

@Data
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private boolean completed;
}

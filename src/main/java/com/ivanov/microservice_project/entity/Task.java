package com.ivanov.microservice_project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ivanov.microservice_project.entity.enums.Priority;
import com.ivanov.microservice_project.entity.enums.TaskStatus;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean completed;
    private double cost; // Стоимость задачи

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @JsonIgnore
    @ManyToMany
    private List<Task> dependencies = new ArrayList<>();

    @JsonIgnore
    @ManyToOne
    private User assignedUser;

    @JsonIgnore
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @JsonIgnore
    @ManyToOne
    private Project project;

    @ManyToMany
    @JoinTable(
            name = "task_material",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "material_id")
    )
    private List<Material> materials;

    public double calculateMaterialCost() {
        return materials.stream().mapToDouble(m -> m.getQuantity() * m.getUnitCost()).sum();
    }
}

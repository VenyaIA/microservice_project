package com.ivanov.microservice_project.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int quantity;
    private String unit;
    private double unitCost; // Стоимость единицы материала

    @ManyToMany(mappedBy = "materials")
    private List<Task> tasks;
}

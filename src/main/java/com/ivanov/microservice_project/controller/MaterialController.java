package com.ivanov.microservice_project.controller;

import com.ivanov.microservice_project.entity.Material;
import com.ivanov.microservice_project.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materials")
public class MaterialController {
    @Autowired
    private MaterialService materialService;

    @GetMapping
    public List<Material> getAllMaterials() {
        return materialService.getAllMaterials();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Material> getMaterialById(@PathVariable Long id) {
        Material material = materialService.getMaterialById(id);
        if (material != null) {
            return ResponseEntity.ok(material);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Material createMaterial(@RequestBody Material material) {
        return materialService.createMaterial(material);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Material> updateMaterial(@PathVariable Long id, @RequestBody Material materialDetails) {
        Material updatedMaterial = materialService.updateMaterial(id, materialDetails);
        if (updatedMaterial != null) {
            return ResponseEntity.ok(updatedMaterial);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaterial(@PathVariable Long id) {
        materialService.deleteMaterial(id);
        return ResponseEntity.noContent().build();
    }
}

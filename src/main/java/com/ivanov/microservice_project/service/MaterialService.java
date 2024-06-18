package com.ivanov.microservice_project.service;

import com.ivanov.microservice_project.entity.Material;
import com.ivanov.microservice_project.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialService {
    @Autowired
    private MaterialRepository materialRepository;

    public List<Material> getAllMaterials() {
        return materialRepository.findAll();
    }

    public Material getMaterialById(Long id) {
        return materialRepository.findById(id).orElse(null);
    }

    public Material createMaterial(Material material) {
        return materialRepository.save(material);
    }

    public Material updateMaterial(Long id, Material materialDetails) {
        Material material = materialRepository.findById(id).orElse(null);
        if (material != null) {
            material.setName(materialDetails.getName());
            material.setQuantity(materialDetails.getQuantity());
            material.setUnit(materialDetails.getUnit());
            return materialRepository.save(material);
        }
        return null;
    }

    public void deleteMaterial(Long id) {
        materialRepository.deleteById(id);
    }

    public Material updateMaterialQuantity(Long id, int quantity) {
        Material material = materialRepository.findById(id).orElse(null);
        if (material != null) {
            material.setQuantity(material.getQuantity() - quantity);
            return materialRepository.save(material);
        }
        return null;
    }
}

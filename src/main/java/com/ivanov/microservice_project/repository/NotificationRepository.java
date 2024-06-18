package com.ivanov.microservice_project.repository;


import com.ivanov.microservice_project.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // Здесь можно добавить кастомные методы, если потребуется
}

package com.ivanov.microservice_project.service;

import com.ivanov.microservice_project.entity.Notification;
import com.ivanov.microservice_project.entity.User;
import com.ivanov.microservice_project.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public Notification sendNotification(User user, String message) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setCreatedAt(LocalDateTime.now());
        return notificationRepository.save(notification);
    }
}

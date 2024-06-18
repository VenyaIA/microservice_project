package com.ivanov.microservice_project.controller;


import com.ivanov.microservice_project.entity.Notification;
import com.ivanov.microservice_project.entity.User;
import com.ivanov.microservice_project.service.NotificationService;
import com.ivanov.microservice_project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<Notification> sendNotification(@RequestParam Long userId, @RequestParam String message) {
        User user = userService.getUserById(userId);
        Notification notification = notificationService.sendNotification(user, message);
        return new ResponseEntity<>(notification, HttpStatus.CREATED);
    }
}

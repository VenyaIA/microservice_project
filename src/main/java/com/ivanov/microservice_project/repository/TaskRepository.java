package com.ivanov.microservice_project.repository;


import com.ivanov.microservice_project.entity.Task;
import com.ivanov.microservice_project.entity.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> getTaskById(Long taskId);

    // Возвращает список всех задач, связанных с указанным проектом.
    List<Task> findByProjectId(Long projectId);
    // Возвращает список задач по указанному проекту, фильтруя по статусу завершенности.
    List<Task> findByProjectIdAndCompleted(Long projectId, boolean completed);

    // Можно добавить другие кастомные методы поиска по различным критериям

    List<Task> findByProjectIdAndStatus(Long projectId, TaskStatus status);

}

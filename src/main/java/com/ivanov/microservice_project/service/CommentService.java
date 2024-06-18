package com.ivanov.microservice_project.service;


import com.ivanov.microservice_project.entity.Comment;
import com.ivanov.microservice_project.exception.ResourceNotFoundException;
import com.ivanov.microservice_project.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsByProjectId(Long projectId) {
        return commentRepository.findByProjectId(projectId);
    }

    public List<Comment> getCommentsByTaskId(Long taskId) {
        return commentRepository.findByTaskId(taskId);
    }

    public Comment updateComment(Long commentId, Comment updatedComment) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));
        comment.setContent(updatedComment.getContent());
        // Другие поля комментария, которые могут быть обновлены
        return commentRepository.save(comment);
    }

    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));
        commentRepository.delete(comment);
    }
}

package com.ivanov.microservice_project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ivanov.microservice_project.entity.enums.EnumRole;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Data
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @Column(unique = true, updatable = false)
    private String username;

    @Column(unique = true)
    private String position;

    @Column(unique = true)
    private String email;

    @Column(length = 3000)
    private String password;

    @Column(nullable = false)
    private String phoneNumber;

    private double hourlyRate; // Почасовая ставка

    @ElementCollection(targetClass = EnumRole.class)
    @CollectionTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"))
    private Set<EnumRole> roles = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Notification> notifications = new ArrayList<>();

    @OneToMany(mappedBy = "assignedUser")
    private List<Task> assignedTasks = new ArrayList<>();

    @Column(updatable = false)
    private LocalDateTime createdDate;

    @PrePersist // задаёт значение атрибута перед тем как мы сделаем новую запись в БД
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }

    public User() {
    }

    public User(Long id,
                    String username,
                    String email,
                    String password,
                    Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public void addTask(Task task) {
        if (assignedTasks == null) {
            assignedTasks = new ArrayList<>();
        }
        assignedTasks.add(task);
    }

    /**
     * SECURITY
     */

    @Transient
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // Геттеры и сеттеры
    // Не забудьте также переопределить методы equals() и hashCode(), если это необходимо
}

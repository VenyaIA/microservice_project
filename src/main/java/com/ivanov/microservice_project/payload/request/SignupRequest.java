package com.ivanov.microservice_project.payload.request;


import com.ivanov.microservice_project.annotations.PasswordMatches;
import lombok.Data;

import javax.validation.constraints.*;

@Data
@PasswordMatches
public class SignupRequest {

    @Email(message = "It should have email format")
    @NotBlank(message = "User email is required")
    private String email;

    @NotEmpty(message = "Please enter your name")
    private String firstname;

    @NotEmpty(message = "Please enter your lastname")
    private String lastname;

    @NotEmpty(message = "Please enter your username")
    private String username;

    @NotEmpty(message = "Please enter your phoneNumber")
    private String phoneNumber;

    @NotEmpty(message = "Please enter your position")
    private String position;

    @NotNull(message = "Please enter hourlyRate")
    private double hourlyRate;

    @NotEmpty(message = "Password is required")
    @Size(min = 6)
    private String password;
    private String confirmPassword;

}

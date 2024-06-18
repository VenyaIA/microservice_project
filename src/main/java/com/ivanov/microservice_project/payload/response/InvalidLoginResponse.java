package com.ivanov.microservice_project.payload.response;

import lombok.Getter;

@Getter
public class InvalidLoginResponse {

    private String username;
    private String password;

    public InvalidLoginResponse() {
        this.username = "Invalid Username";
        this.password = "invalid Password";
    }

}

package com.ivanov.microservice_project.validations;

import com.ivanov.microservice_project.annotations.PasswordMatches;
import com.ivanov.microservice_project.payload.request.SignupRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        SignupRequest userSignupRequest = (SignupRequest) o;
        return userSignupRequest.getPassword().equals(userSignupRequest.getConfirmPassword());
    }
}

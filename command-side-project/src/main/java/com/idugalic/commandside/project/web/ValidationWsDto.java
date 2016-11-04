package com.idugalic.commandside.project.web;

import java.io.Serializable;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.axonframework.messaging.interceptors.JSR303ViolationException;

public class ValidationWsDto implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 8491458717241390792L;
    private Set<ConstraintViolation<Object>> violations;

    public ValidationWsDto() {
        super();
    }

    public ValidationWsDto(JSR303ViolationException e) {
        this.violations = e.getViolations();
    }

    public Set<ConstraintViolation<Object>> getViolations() {
        return violations;
    }

    public void setViolations(Set<ConstraintViolation<Object>> violations) {
        this.violations = violations;
    }

}

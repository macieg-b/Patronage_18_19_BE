package mba.bookingsystem.exception;

import org.springframework.http.HttpStatus;


class ErrorResponseEntity {
    private final HttpStatus status;
    private final String resourceName;

    ErrorResponseEntity(HttpStatus status, String resourceName) {
        this.status = status;
        this.resourceName = resourceName;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getResourceName() {
        return resourceName;
    }
}
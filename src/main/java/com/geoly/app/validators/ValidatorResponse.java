package com.geoly.app.validators;

import com.geoly.app.models.StatusMessage;
import org.springframework.http.HttpStatus;

public class ValidatorResponse {

    private boolean isValid;
    private HttpStatus httpStatus;
    private StatusMessage statusMessage;

    public ValidatorResponse(boolean isValid, HttpStatus httpStatus, StatusMessage statusMessage) {
        this.isValid = isValid;
        this.httpStatus = httpStatus;
        this.statusMessage = statusMessage;
    }

    public ValidatorResponse(boolean isValid) {
        this.isValid = isValid;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public StatusMessage getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(StatusMessage statusMessage) {
        this.statusMessage = statusMessage;
    }
}

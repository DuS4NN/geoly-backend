package com.geoly.app.dao;

import com.geoly.app.models.StatusMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class Response {

    private ResponseEntity responseEntity;
    private List data;

    public Response(StatusMessage statusMessage, HttpStatus httpStatus, List data) {
        this.responseEntity = new ResponseEntity<>(statusMessage, httpStatus);
        this.data = data;
    }

    public Response() {
    }

    public ResponseEntity getResponseEntity() {
        return responseEntity;
    }

    public void setResponseEntity(ResponseEntity responseEntity) {
        this.responseEntity = responseEntity;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }
}

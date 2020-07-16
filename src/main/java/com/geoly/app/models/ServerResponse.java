package com.geoly.app.models;

public class ServerResponse {

    private Enum<ServerResponseType> type;
    private Enum<ServerResponseMessage> message;

    public ServerResponse(Enum<ServerResponseType> type, Enum<ServerResponseMessage> message) {
        this.type = type;
        this.message = message;
    }

    public Enum<ServerResponseType> getType() {
        return type;
    }

    public void setType(Enum<ServerResponseType> type) {
        this.type = type;
    }

    public Enum<ServerResponseMessage> getMessage() {
        return message;
    }

    public void setMessage(Enum<ServerResponseMessage> message) {
        this.message = message;
    }
}

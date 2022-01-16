package com.geekbrains.model;

public class AuthenticationError implements AbstractMessage {


    public String getInfo() {
        return info;
    }

    private final String info;

    public AuthenticationError(String info) {
        this.info = info;
    }


    @Override
    public Commands getMessageType() {
        return Commands.AUTHENTICATION_ERROR;
    }
}

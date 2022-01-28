package com.geekbrains;

public class AuthenticationError implements AbstractMessage {

    @Override
    public Commands getMessageType() {
        return Commands.AUTHENTICATION_ERROR;
    }
}

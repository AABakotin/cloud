package com.geekbrains;

public class ChangePasswordError implements AbstractMessage {

    @Override
    public Commands getMessageType() {
        return Commands.CHANGE_PASSWORD_ERROR;
    }
}

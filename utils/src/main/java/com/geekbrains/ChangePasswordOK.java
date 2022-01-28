package com.geekbrains;

public class ChangePasswordOK implements AbstractMessage {

    @Override
    public Commands getMessageType() {
        return Commands.CHANGE_PASSWORD_OK;
    }
}

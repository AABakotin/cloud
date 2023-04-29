package com.geekbrains.message.impl;

import com.geekbrains.Commands;
import com.geekbrains.message.AbstractMessage;

public class ChangePasswordError implements AbstractMessage {

    @Override
    public Commands getMessageType() {
        return Commands.CHANGE_PASSWORD_ERROR;
    }
}

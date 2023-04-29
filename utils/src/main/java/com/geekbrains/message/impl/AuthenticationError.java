package com.geekbrains.message.impl;

import com.geekbrains.Commands;
import com.geekbrains.message.AbstractMessage;

public class AuthenticationError implements AbstractMessage {

    @Override
    public Commands getMessageType() {
        return Commands.AUTHENTICATION_ERROR;
    }
}

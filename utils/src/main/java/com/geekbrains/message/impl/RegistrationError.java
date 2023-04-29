package com.geekbrains.message.impl;

import com.geekbrains.Commands;
import com.geekbrains.message.AbstractMessage;

public class RegistrationError implements AbstractMessage {

    public RegistrationError(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    private final String info;


    @Override
    public Commands getMessageType() {
        return Commands.REGISTRATION_ERROR;
    }
}

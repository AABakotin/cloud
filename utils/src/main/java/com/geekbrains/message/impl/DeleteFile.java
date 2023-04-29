package com.geekbrains.message.impl;

import com.geekbrains.Commands;
import com.geekbrains.message.AbstractMessage;

public class DeleteFile implements AbstractMessage {

    public String getName() {
        return name;
    }

    private final String name;

    public DeleteFile(String name) {
        this.name = name;
    }

    @Override
    public Commands getMessageType() {
        return Commands.DELETE_FILE;
    }
}

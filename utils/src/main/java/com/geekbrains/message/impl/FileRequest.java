package com.geekbrains.message.impl;

import com.geekbrains.Commands;
import com.geekbrains.message.AbstractMessage;
import lombok.Data;

@Data
public class FileRequest implements AbstractMessage {

    private final String fileName;

    @Override
    public Commands getMessageType() {
        return Commands.FILE_REQUEST;
    }
}

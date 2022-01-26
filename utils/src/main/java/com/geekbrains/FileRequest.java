package com.geekbrains;

import lombok.Data;

@Data
public class FileRequest implements AbstractMessage {

    private final String fileName;

    @Override
    public Commands getMessageType() {
        return Commands.FILE_REQUEST;
    }
}

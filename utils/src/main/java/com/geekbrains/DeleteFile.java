package com.geekbrains;

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

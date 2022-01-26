package com.geekbrains;

public class CreateFolder implements AbstractMessage{

    private String name;

    public CreateFolder(String name) {
        this.name = name;
    }

    @Override
    public Commands getMessageType() {
        return Commands.CREATE_FOLDER;
    }

    public String getName() {
        return name;
    }
}

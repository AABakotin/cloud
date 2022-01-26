package com.geekbrains;

public class RequestFolder implements AbstractMessage {

    private String folder;

    public RequestFolder(String folder) {
        this.folder = folder;
    }

    public String getFolder() {
        return folder;
    }

    @Override
    public Commands getMessageType() {
        return Commands.FOLDER_REQUEST;
    }
}

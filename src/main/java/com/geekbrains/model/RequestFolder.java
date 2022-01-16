package com.geekbrains.model;

public class RequestFolder implements AbstractMessage {



    private String folder;




    public String getFolder() {
        return folder;
    }




    public RequestFolder(String folder) {
        this.folder = folder;
    }


    @Override
    public Commands getMessageType() {
        return Commands.FOLDER_REQUEST;
    }
}

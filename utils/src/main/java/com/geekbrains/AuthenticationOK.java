package com.geekbrains;

public class AuthenticationOK implements AbstractMessage{

    private final String locationPath;


    public AuthenticationOK(String locationPath) {
        this.locationPath = locationPath;
    }


    public String getLocationPath() {
        return locationPath;
    }


    @Override
    public Commands getMessageType() {
        return Commands.AUTHENTICATION_OK;
    }
}

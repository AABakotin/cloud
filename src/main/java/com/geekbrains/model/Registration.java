package com.geekbrains.model;

public class Registration implements AbstractMessage{

    String login;
    String password;

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Registration(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Override
    public Commands getMessageType() {
        return Commands.REGISTRATION;
    }
}

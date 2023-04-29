package com.geekbrains.message.impl;

import com.geekbrains.Commands;
import com.geekbrains.message.AbstractMessage;

public class DeleteAccount implements AbstractMessage {


    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    private final String login;
    private final String password;

    public DeleteAccount(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Override
    public Commands getMessageType() {
        return Commands.DELETE_ACCOUNT;
    }
}

package com.geekbrains.message.impl;

import com.geekbrains.Commands;
import com.geekbrains.message.AbstractMessage;

public class ChangePassword implements AbstractMessage {

    public String getNewPassword() {
        return newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getLogin() {
        return login;
    }

    private final String newPassword;
    private final String oldPassword;
    private final String login;





    public ChangePassword(String newPassword, String oldPassword, String login) {
        this.newPassword = newPassword;
        this.oldPassword = oldPassword;
        this.login = login;
    }


    @Override
    public Commands getMessageType() {
        return Commands.CHANGE_PASSWORD;
    }


}

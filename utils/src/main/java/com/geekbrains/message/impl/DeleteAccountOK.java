package com.geekbrains.message.impl;

import com.geekbrains.Commands;
import com.geekbrains.message.AbstractMessage;

public class DeleteAccountOK implements AbstractMessage {


    @Override
    public Commands getMessageType() {
        return Commands.DELETE_ACCOUNT_OK;
    }
}

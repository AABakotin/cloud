package com.geekbrains;

public class DeleteAccountOK implements AbstractMessage{


    @Override
    public Commands getMessageType() {
        return Commands.DELETE_ACCOUNT_OK;
    }
}

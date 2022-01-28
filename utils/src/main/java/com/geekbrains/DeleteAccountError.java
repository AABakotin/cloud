package com.geekbrains;

public class DeleteAccountError implements AbstractMessage{
    @Override
    public Commands getMessageType() {
        return Commands.DELETE_ACCOUNT_ERROR;
    }
}

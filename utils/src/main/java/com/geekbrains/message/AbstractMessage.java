package com.geekbrains.message;

import com.geekbrains.Commands;

import java.io.Serializable;

public interface AbstractMessage extends Serializable {

    Commands getMessageType();

}

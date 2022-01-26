package com.geekbrains;

import java.io.Serializable;

public interface AbstractMessage extends Serializable {

    Commands getMessageType();

}

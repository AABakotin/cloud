package com.geekbrains.message.impl;

import com.geekbrains.Commands;
import com.geekbrains.message.AbstractMessage;
import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class FilesList implements AbstractMessage {

    private final List<File> files;

    public FilesList(Path path) throws IOException {
        files = Files.list(path)
                .map(Path::toFile)
                .collect(Collectors.toList());
    }

    @Override
    public Commands getMessageType() {
        return Commands.FILES_LIST;
    }
}

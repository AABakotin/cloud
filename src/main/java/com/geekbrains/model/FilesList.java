package com.geekbrains.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;

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

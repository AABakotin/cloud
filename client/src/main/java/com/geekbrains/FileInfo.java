package com.geekbrains;

import javafx.scene.image.ImageView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class FileInfo {

    private final String fileName;
    private final boolean isDirectory;
    private final long itemSize;
    private final String type;
    private LocalDateTime data;
    private ImageView iconType;

    public String getPathName() {
        return pathName;
    }

    private String pathName;

    public FileInfo(Path path) {
        fileName = path.getFileName().toString();
        isDirectory = Files.isDirectory(path);
        if (!isDirectory) {
            itemSize = path.toFile().length();
            type = "File";
        } else {
            type = "DIR";
            itemSize = -1;
        }
        try {
            data = LocalDateTime.ofInstant(
                    Files.getLastModifiedTime(path)
                            .toInstant(),
                    ZoneOffset.ofHours(3));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public LocalDateTime getData() {
        return data;
    }

    public String toString() {
        return getType() + " "
                + getFileName() + " "
                + getItemSize() + " "
                + getData();
    }

    public String getType() {
        return type;
    }


    public String getFileName() {
        return fileName;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public Long getItemSize() {
        return itemSize;
    }

}

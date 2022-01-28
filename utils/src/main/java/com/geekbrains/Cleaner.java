package com.geekbrains;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class Cleaner {

    private static final Logger logger = LoggerFactory.getLogger(Cleaner.class);




    public static void delete (File file) {
        if (!file.exists())
            return;
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                delete(f);
            }
        }
        file.delete();
        logger.info("Deleted file/folder: "+file.getAbsolutePath());
    }
}

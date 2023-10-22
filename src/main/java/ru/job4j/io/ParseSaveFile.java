package ru.job4j.io;

import java.io.*;

public class ParseSaveFile {

    private final File file;

    public ParseSaveFile(File file) {
        this.file = file;
    }

    public void saveContent(String content) throws IOException {
        try (BufferedOutputStream outputStream =
                     new BufferedOutputStream(new FileOutputStream(file))) {
            for (int i = 0; i < content.length(); i++) {
                outputStream.write(content.charAt(i));
            }
        }
    }
}

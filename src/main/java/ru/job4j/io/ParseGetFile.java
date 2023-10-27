package ru.job4j.io;

import java.io.*;
import java.util.function.Predicate;

public class ParseGetFile {

    private final File file;

    public ParseGetFile(File file) {
        this.file = file;
    }

    public String getContent() throws IOException {
        return getContentWithPredicate(e -> true);
    }

    public String getContentWithoutUnicode() throws IOException {
        return getContentWithPredicate(e -> e < 0x80);
    }

    private String getContentWithPredicate(Predicate<Integer> predicate) throws IOException {
        StringBuilder result = new StringBuilder();
        try (BufferedInputStream inputStream =
                     new BufferedInputStream(new FileInputStream(file))) {
            int data;
            while ((data = inputStream.read()) != -1) {
                if (predicate.test(data)) {
                    result.append((char) data);
                }
            }
        }
        return result.toString();
    }
}

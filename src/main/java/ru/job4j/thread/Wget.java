package ru.job4j.thread;

import org.apache.commons.validator.routines.UrlValidator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class Wget implements Runnable {
    private static final int NANO_IN_MS = 1_000_000;
    private static final int SPEED_DOWNLOAD = 512;
    private final String url;
    private final int speed;
    private final String filePath;

    public Wget(String url, int speed, String filePath) {
        this.url = url;
        this.speed = speed;
        this.filePath = filePath;
    }

    @Override
    public void run() {
        var file = new File(filePath);
        try (var in = new URL(url).openStream();
        var out = new FileOutputStream(file)) {
            var dataBuffer = new byte[SPEED_DOWNLOAD];
            int bytesRead;
            System.out.println("Begin load.");
            var downloadAt = System.currentTimeMillis();
            var totalRead = 0;
            var delay = 0L;
            while ((bytesRead = in.read(dataBuffer, 0, dataBuffer.length)) != -1) {
                out.write(dataBuffer, 0, bytesRead);
                totalRead += bytesRead;
                if (totalRead >= speed) {
                    delay = System.currentTimeMillis() - downloadAt;
                    if (delay < speed) {
                        System.out.printf("Byte read: %d delay: %d ms%n", totalRead, delay);
                        Thread.sleep(speed - delay);
                    }
                    totalRead = 0;
                    downloadAt = System.currentTimeMillis();
                }

            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length != 3) {
            throw new IllegalArgumentException("Not all arguments");
        }
        String url = args[0];
        int speed = Integer.parseInt(args[1]);
        String file = args[2];
        UrlValidator validator = new UrlValidator();
        if (!validator.isValid(url)) {
            throw new IllegalArgumentException("url not valid");
        }
        if (speed <= 0) {
            throw new InterruptedException("speed <= 0");
        }
        Thread wget = new Thread(new Wget(url, speed, file));
        wget.start();
        wget.join();
    }
}

package ru.job4j.thread;

import org.apache.commons.validator.routines.UrlValidator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class Wget implements Runnable {
    private final String url;
    private final int speed;
    private final String filePath;
    private final int ms = 1_000_000;

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
            var dataBuffer = new byte[speed];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, dataBuffer.length)) != -1) {
                var downloadAt = System.nanoTime();
                out.write(dataBuffer, 0, bytesRead);
                var timeLoad = System.nanoTime() - downloadAt;
                var delay = timeLoad < ms ? ms - timeLoad : 0;
                Thread.sleep(delay / 1000);
                System.out.printf("Read %d bytes : %d delay : %d%n", speed,
                        timeLoad, delay);
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
        String file = args[1];
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

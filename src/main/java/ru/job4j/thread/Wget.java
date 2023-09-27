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

    public Wget(String url, int speed, String filePath) {
        this.url = url;
        this.speed = speed;
        this.filePath = filePath;
    }

    @Override
    public void run() {
        var file = new File(filePath);
        var ms = 1000;
        try (var in = new URL(url).openStream();
        var out = new FileOutputStream(file)) {
            var dataBuffer = new byte[speed];
            int bytesRead;
            var totalRead = 0;
            System.out.println("Begin load.");
            var downloadAt = System.nanoTime();
            while ((bytesRead = in.read(dataBuffer, 0, dataBuffer.length)) != -1) {
                out.write(dataBuffer, 0, bytesRead);
                totalRead += bytesRead;
            }
            var timeSleep = totalRead / speed * ms + totalRead % speed;
            var timeLoadMs = (System.nanoTime() - downloadAt) / ms;
            System.out.printf("Total read: %d Time load: %d Load ms: %d", totalRead,
                    timeSleep, timeLoadMs);
            if (timeSleep > timeLoadMs) {
                Thread.sleep(timeSleep - timeLoadMs);
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

package com.h2o.poppy.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class DirectoryService {

    public synchronized int createDirectory(String address) {
        String[] words = address.split(" ");

        String basePath = "/app/data";

        File baseDirectory = new File(basePath);
        if (!baseDirectory.exists()) {
            baseDirectory.mkdirs();
        }

        StringBuilder directoryPathBuilder = new StringBuilder(basePath);
        for (String word : words) {
            directoryPathBuilder.append(File.separator).append(word);
        }

        String directoryPath = directoryPathBuilder.toString();
        System.out.println("Directory Path: " + directoryPath);

        Path path = Paths.get(directoryPath);
        if (Files.exists(path)) {
            System.out.println("Directory already exists.");
            return 2;
        }
        try {
            Files.createDirectories(path);
            System.out.println("Directory created successfully.");
            return 1;
        } catch (IOException e) {
            System.out.println("Failed to create directory due to an error: " + e.getMessage());
            return 0;
        }
    }
}

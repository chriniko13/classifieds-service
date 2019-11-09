package com.chriniko.classifieds.service.test.infra;

import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class FileSupport {

    public static String readResource(String resourceName) {
        try {
            URL url = FileSupport.class.getClassLoader().getResource(resourceName);
            URI uri = Objects.requireNonNull(url).toURI();
            Path path = Paths.get(uri);
            return String.join("", Files.readAllLines(path));
        } catch (Exception e) {
            throw new TestInfraException(e);
        }
    }

}

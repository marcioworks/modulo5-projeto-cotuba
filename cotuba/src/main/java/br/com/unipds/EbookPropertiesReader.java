package br.com.unipds;

import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

@ApplicationScoped
public class EbookPropertiesReader implements PropertiesReader {

    @Override
    public EbookProperties readProperties(Path directoryPath) {
        Path propertiesFile = directoryPath.resolve("ebook.properties");
        if (!Files.exists(propertiesFile)) {
            throw new IllegalArgumentException("Properties file not found: " + propertiesFile.toString());
        }

        Properties properties = new Properties();
        try (InputStream input = Files.newInputStream(propertiesFile)) {
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("error read properties", e);
        }

        return new EbookProperties(properties.getProperty("cotuba.ebook.titulo"), properties.getProperty("cotuba.ebook.autor"));
    }
}

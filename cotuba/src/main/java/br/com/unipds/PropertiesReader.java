package br.com.unipds;

import java.nio.file.Path;

public interface PropertiesReader {
    void readProperties(Path directoryPath, Ebook ebook);
}

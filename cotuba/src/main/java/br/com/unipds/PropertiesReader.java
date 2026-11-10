package br.com.unipds;

import java.nio.file.Path;

public interface PropertiesReader {
    EbookProperties readProperties(Path directoryPath);
}

package br.com.unipds;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface MarkdownReader {
    void readFiles(List<Chapter> chapters) throws IOException;
}

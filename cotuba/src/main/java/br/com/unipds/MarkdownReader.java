package br.com.unipds;

import java.io.IOException;
import java.util.List;

public interface MarkdownReader {
    List<Chapter> readFiles(List<Markdown> markdowns) throws IOException;
}

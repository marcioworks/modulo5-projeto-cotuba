package br.com.unipds;

import java.nio.file.Path;
import java.util.List;

public class Ebook {
    private String title;
    private String author;
    private List<Chapter> chapters;
    private EbookExtension format;
    private Path filePath;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }

    public EbookExtension getFormat() {
        return format;
    }

    public void setFormat(EbookExtension format) {
        this.format = format;
    }

    public Path getFilePath() {
        return filePath;
    }

    public void setFilePath(Path filePath) {
        this.filePath = filePath;
    }
}

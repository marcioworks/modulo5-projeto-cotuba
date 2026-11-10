package br.com.unipds;

import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@EbookFormatQualifier(EbookExtension.HTML)
@ApplicationScoped
public class GenerateHtml implements GenerateEbook {
    @Override
    public void generate(Ebook ebook) {
        Path filePath = ebook.filePath();

        try {
            Path directory = Files.createDirectories(filePath);
            Map<Chapter, Path> fileChapterHtm = new LinkedHashMap<>();
            int i = 1;
            for (Chapter chapter : ebook.chapters()) {
                String fileHtmlName = getFileNameHtml(i, chapter);
                Path htmlFile = directory.resolve(fileHtmlName);
                writeFileHtml(htmlFile, chapter);
                fileChapterHtm.put(chapter, htmlFile);
                i++;
            }
            writeSummary(ebook, directory, fileChapterHtm);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeSummary(Ebook ebook, Path directory, Map<Chapter, Path> fileChapterHtm) throws IOException {
        String itens = ebook.chapters().stream().map(chapter -> {
            String fileName = fileChapterHtm.get(chapter)
                    .getFileName()
                    .toString()
                    .replace("\\", "/");

            return """
                    <li><a href="%s">%s</a></li>
                    """.formatted(fileName, chapter.title());
        }).collect(Collectors.joining());

        String html = """
                <!Doctype html>
                <html lang = "pt-br" >
                <head>
                    <meta charset = "UTF-8" >
                    <meta name = "viewport" content = "width=device-width, initial-scale=1.0" >
                    <title> %s </title >
                </head >
                    <body >
                        <h1> %s </h1 >
                        <h2> Autor: %s </h2 >
                        <h3> Sumário </h3 >
                        <ul>
                             %s
                        </ul >
                    </body >
                </html >
                """.formatted(ebook.title(), ebook.title(), ebook.author(), itens);

        Path index = directory.resolve("index.html");
        Files.writeString(index, html, StandardCharsets.UTF_8);
    }

    private void writeFileHtml(Path htmlFile, Chapter chapter) throws IOException {
        String html = """
                <!Doctype html >
                <html lang = "pt-br">
                <head>
                    <meta charset = "UTF-8" >
                    <meta name = "viewport" content = "width=device-width, initial-scale=1.0" >
                    <title> %s </title >
                </head >
                    <body >
                         %s
                    </body >
                </html >
                """.formatted(chapter.title(), chapter.htmlContent());

        Files.writeString(htmlFile, html, StandardCharsets.UTF_8);
    }

    private String getFileNameHtml(int i, Chapter chapter) {
        String cleanTitle = chapter.title().replaceAll("\\W", "");
        return "%d-%s.html".formatted(i, cleanTitle);
    }
}

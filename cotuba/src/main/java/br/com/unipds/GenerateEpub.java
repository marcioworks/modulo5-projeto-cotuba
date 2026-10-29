package br.com.unipds;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.GuideReference;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubWriter;
import nl.siegmann.epublib.service.MediatypeService;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@ApplicationScoped
@Named("generateEpub")
public class GenerateEpub implements GenerateEbook {

    @Override
    public void generate(Ebook ebook) {
        try {
            var epub = new Book();
            epub.getMetadata().addTitle(ebook.getTitle());
            epub.getMetadata().addAuthor(new Author(ebook.getAuthor()));

            boolean[] ehPrimeiroCapitulo = {true};
            List<Chapter> chapters = ebook.getChapters();
            chapters.forEach(chapter -> {

                String title = chapter.getTitle();
                String htmlContent = chapter.getHtmlContent();
                // TODO: usar título do capítulo
                String epubHtml = """
                          <html xmlns="http://www.w3.org/1999/xhtml">
                            <head>
                              <title>%s</title>
                            </head>
                            <body>
                              %s
                            </body>
                          </html>
                        """.formatted(title,htmlContent);
                var chapterPub = new Resource(epubHtml.getBytes(), MediatypeService.XHTML);
                epub.addSection(title, chapterPub);

                if (ehPrimeiroCapitulo[0]) {
                    epub.getGuide().addReference(new GuideReference(chapterPub, "text", "Start Reading"));
                    ehPrimeiroCapitulo[0] = false;
                }
            });

            var epubWriter = new EpubWriter();

            try {
                epubWriter.write(epub, Files.newOutputStream(ebook.getFilePath()));
            } catch (IOException ex) {
                throw new IllegalStateException("Erro ao criar arquivo EPUB: " + ebook.getFilePath().toAbsolutePath(), ex);
            }

        } catch (
                Exception ex) {
            throw new IllegalStateException("Erro ao gerar EPUB: " + ebook.getFilePath().toAbsolutePath(), ex);
        }
    }
}

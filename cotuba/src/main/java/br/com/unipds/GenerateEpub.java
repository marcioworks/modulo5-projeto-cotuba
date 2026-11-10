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
@EbookFormatQualifier(EbookExtension.EPUB)
public class GenerateEpub implements GenerateEbook {

    @Override
    public void generate(Ebook ebook) {
        try {
            var epub = new Book();
            epub.getMetadata().addTitle(ebook.title());
            epub.getMetadata().addAuthor(new Author(ebook.author()));

            boolean[] ehPrimeiroCapitulo = {true};
            List<Chapter> chapters = ebook.chapters();
            chapters.forEach(chapter -> {

                String title = chapter.title();
                String htmlContent = chapter.htmlContent();
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
                epubWriter.write(epub, Files.newOutputStream(ebook.filePath()));
            } catch (IOException ex) {
                throw new IllegalStateException("Erro ao criar arquivo EPUB: " + ebook.filePath().toAbsolutePath(), ex);
            }

        } catch (
                Exception ex) {
            throw new IllegalStateException("Erro ao gerar EPUB: " + ebook.filePath().toAbsolutePath(), ex);
        }
    }
}

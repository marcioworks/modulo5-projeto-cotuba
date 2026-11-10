package br.com.unipds;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

import java.io.IOException;
import java.util.List;

@ApplicationScoped
public class CotubaService {

    private final MarkdownReader markdownReader;
    private final PropertiesReader propertiesReader;
    private final MarkDownRepository markDownRepository;
    private final Instance<GenerateEbook> generateEbookInstance;


    @Inject
    public CotubaService(MarkdownReader markdownReader, PropertiesReader propertiesReader, MarkDownRepository markDownRepository,
                        @Any Instance<GenerateEbook> generateEbook) {
        this.markdownReader = markdownReader;
        this.propertiesReader = propertiesReader;
        this.markDownRepository = markDownRepository;
        this.generateEbookInstance = generateEbook;
    }

    public void executar(ParametrosCotuba parametros) throws IOException {

        List<Markdown> markdowns = markDownRepository.getFiles(parametros.diretorioDosMD());
        List<Chapter> chapters = markdownReader.readFiles(markdowns);

        EbookBuilder ebookBuilder = EbookBuilder.builder();
        EbookProperties ebookProperties = propertiesReader.readProperties(parametros.diretorioDosMD());
        ebookBuilder.title(ebookProperties.title());
        ebookBuilder.author(ebookProperties.author());
        ebookBuilder.filePath(parametros.arquivoDeSaida());
        ebookBuilder.chapters(chapters);
        ebookBuilder.format(parametros.formato());


        GenerateEbook generateEbook = generateEbookInstance.select(EbookFormatFiler.of(ebookBuilder.format())).get();
        var ebook = ebookBuilder.build();
        generateEbook.generate(ebook);
    }
}

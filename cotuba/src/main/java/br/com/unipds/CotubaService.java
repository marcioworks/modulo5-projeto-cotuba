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

        List<Chapter> chapterList = markDownRepository.getFiles(parametros.getDiretorioDosMD());
        markdownReader.readFiles(chapterList);

        Ebook ebook = new Ebook();
        ebook.setFilePath(parametros.getArquivoDeSaida());
        ebook.setChapters(chapterList);
        ebook.setFormat(parametros.getFormato());

        propertiesReader.readProperties(parametros.getDiretorioDosMD(), ebook);

        GenerateEbook generateEbook = generateEbookInstance.select(EbookFormatFiler.of(ebook.getFormat())).get();
        generateEbook.generate(ebook);
    }
}

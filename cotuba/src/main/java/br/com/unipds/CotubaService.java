package br.com.unipds;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.IOException;
import java.util.List;

@ApplicationScoped
public class CotubaService {

    private final MarkdownReader markdownReader;
    private final PropertiesReader propertiesReader;
    private final MarkDownRepository markDownRepository;
    private final GenerateEbook generatePdf;
    private final GenerateEbook generateEpub;


    @Inject
    public CotubaService(MarkdownReader markdownReader, PropertiesReader propertiesReader, MarkDownRepository markDownRepository,
                         @Named("generatePDF") GenerateEbook generatePdf, @Named("generateEpub")GenerateEbook generateEpub) {
        this.markdownReader = markdownReader;
        this.propertiesReader = propertiesReader;
        this.markDownRepository = markDownRepository;
        this.generatePdf = generatePdf;
        this.generateEpub = generateEpub;
    }

    public void executar(ParametrosCotuba parametros) throws IOException {

        List<Chapter> chapterList = markDownRepository.getFiles(parametros.getDiretorioDosMD());
        markdownReader.readFiles(chapterList);

        Ebook ebook = new Ebook();
        ebook.setFilePath(parametros.getArquivoDeSaida());
        ebook.setChapters(chapterList);
        ebook.setFormat(parametros.getFormato());

        propertiesReader.readProperties(parametros.getDiretorioDosMD(), ebook);

        GenerateEbook generateEbook ;
        if (EbookExtension.PDF.equals(parametros.getFormato())) {
            generateEbook = generatePdf;
        } else if (EbookExtension.EPUB.equals(parametros.getFormato())) {
            generateEbook = generateEpub;
        } else {
            throw new IllegalArgumentException("Formato do ebook inválido: " + parametros.getFormato());
        }
        generateEbook.generate(ebook);
    }
}

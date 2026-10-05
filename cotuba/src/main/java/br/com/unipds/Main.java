package br.com.unipds;

import java.nio.file.Path;

import nl.siegmann.epublib.domain.*;

public class Main {

    void main(String[] args) {
        int exitCode = executar(args);
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }

    int executar(String[] args) {
        boolean modoVerboso = true;
        try {
            var cliReader = new ReadCliOptions();
            cliReader.read(args);


            Path diretorioDosMD = cliReader.getDiretorioDosMD();
            String formato = cliReader.getFormato();
            Path arquivoDeSaida = cliReader.getArquivoDeSaida();
            modoVerboso = cliReader.modoVerboso;

            MarkdownReader markdownReader = new MarkdownReader();
            var htmls = markdownReader.readFiles(diretorioDosMD);
            if ("pdf".equals(formato)) {
               GeneratePDF geradorPDF = new GeneratePDF();
               geradorPDF.generate(htmls, arquivoDeSaida);

            } else if ("epub".equals(formato)) {
                GenerateEpub geradorEpub = new GenerateEpub();
                geradorEpub.generate(htmls, arquivoDeSaida);
            } else {
                throw new IllegalArgumentException("Formato do ebook inválido: " + formato);
            }

            System.out.println("Arquivo gerado com sucesso: " + arquivoDeSaida);
            return 0;

        } catch (
                Exception ex) {
            System.err.println(ex.getMessage());
            if (modoVerboso) {
                System.err.println();
                ex.printStackTrace();
            }
            return 1;
        }
    }

}
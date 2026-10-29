package br.com.unipds;

import org.apache.commons.cli.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public class ReadCliOptions {

    public ParametrosCotuba read(String[] args) {
        var options = new Options();

        var opcaoDeDiretorioDosMD = new Option("d", "dir", true,
                "Diretório que contém os arquivos md. Default: diretório atual.");
        options.addOption(opcaoDeDiretorioDosMD);

        var opcaoDeFormatoDoEbook = new Option("f", "format", true,
                "Formato de saída do ebook. Pode ser: pdf ou epub. Default: pdf");
        options.addOption(opcaoDeFormatoDoEbook);

        var opcaoDeArquivoDeSaida = new Option("o", "output", true,
                "Arquivo de saída do ebook. Default: book.{formato}.");
        options.addOption(opcaoDeArquivoDeSaida);

        var opcaoModoVerboso = new Option("v", "verbose", false,
                "Habilita modo verboso.");
        options.addOption(opcaoModoVerboso);

        CommandLineParser cmdParser = new DefaultParser();
        var ajuda = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = cmdParser.parse(options, args);
        } catch (ParseException exception) {
            System.err.println(exception.getMessage());
            ajuda.printHelp("cotuba", options);
            throw new IllegalStateException(exception);
        }

        try {
            ParametrosCotuba parametrosCotuba = new ParametrosCotuba();
            boolean modoVerboso = true ;

            String nomeDoDiretorioDosMD = cmd.getOptionValue("dir");

            if (nomeDoDiretorioDosMD != null) {
                parametrosCotuba.setDiretorioDosMD(Paths.get(nomeDoDiretorioDosMD));
                if (!Files.isDirectory(parametrosCotuba.getDiretorioDosMD())) {
                    throw new IllegalArgumentException(nomeDoDiretorioDosMD + " não é um diretório.");
                }
            } else {
                Path diretorioAtual = Paths.get("");
                parametrosCotuba.setDiretorioDosMD(diretorioAtual);
            }

            String nomeDoFormatoDoEbook = cmd.getOptionValue("format");

            if (nomeDoFormatoDoEbook != null) {
                try {
                    parametrosCotuba.setFormato( EbookExtension.valueOf(nomeDoFormatoDoEbook.toUpperCase()));
                }catch (IllegalArgumentException ex){
                    throw new IllegalArgumentException("Formato do ebook inválido: " + nomeDoFormatoDoEbook,ex);
                }
            } else {
                parametrosCotuba.setFormato (EbookExtension.PDF);
            }

            String nomeDoArquivoDeSaidaDoEbook = cmd.getOptionValue("output");
            if (nomeDoArquivoDeSaidaDoEbook != null) {
                parametrosCotuba.setArquivoDeSaida(Paths.get(nomeDoArquivoDeSaidaDoEbook));
            } else {
                parametrosCotuba.setArquivoDeSaida(Paths.get("book." + parametrosCotuba.getFormato().name().toLowerCase()));
            }
            if (Files.isDirectory(parametrosCotuba.getArquivoDeSaida())) {
                // deleta arquivos do diretório recursivamente
                Files.walk(parametrosCotuba.getArquivoDeSaida()).sorted(Comparator.reverseOrder())
                        .map(Path::toFile).forEach(File::delete);
            } else {
                Files.deleteIfExists(parametrosCotuba.getArquivoDeSaida());
            }

            modoVerboso = cmd.hasOption("verbose");
            return parametrosCotuba;
        }catch (Exception exception){
            throw new IllegalArgumentException(exception);
        }
    }
}

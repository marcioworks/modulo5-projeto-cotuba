package br.com.unipds;

import java.nio.file.Path;

public record ParametrosCotuba(
        Path diretorioDosMD,
        EbookExtension formato,
        Path arquivoDeSaida,
        boolean modoVerboso
) {
    ParametrosCotuba(Path diretorioDosMD, EbookExtension formato, Path arquivoDeSaida){
        this(diretorioDosMD, formato, arquivoDeSaida, false);
    }
}

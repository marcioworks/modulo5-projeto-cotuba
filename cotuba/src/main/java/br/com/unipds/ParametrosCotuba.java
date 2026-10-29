package br.com.unipds;

import java.nio.file.Path;

public class ParametrosCotuba {

    private Path diretorioDosMD;
    private EbookExtension formato;
    private Path arquivoDeSaida;
    boolean modoVerboso = false;

    public Path getDiretorioDosMD() {
        return diretorioDosMD;
    }

    public void setDiretorioDosMD(Path diretorioDosMD) {
        this.diretorioDosMD = diretorioDosMD;
    }

    public EbookExtension getFormato() {
        return formato;
    }

    public void setFormato(EbookExtension formato) {
        this.formato = formato;
    }

    public Path getArquivoDeSaida() {
        return arquivoDeSaida;
    }

    public void setArquivoDeSaida(Path arquivoDeSaida) {
        this.arquivoDeSaida = arquivoDeSaida;
    }

    public boolean isModoVerboso() {
        return modoVerboso;
    }

    public void setModoVerboso(boolean modoVerboso) {
        this.modoVerboso = modoVerboso;
    }
}

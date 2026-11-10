package br.com.unipds;

import java.nio.file.Path;
import java.util.List;

public interface MarkDownRepository {
    List<Markdown> getFiles(Path diretorioDosMD);

}

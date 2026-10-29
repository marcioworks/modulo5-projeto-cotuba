package br.com.unipds;

import java.nio.file.Path;
import java.util.List;

public interface MarkDownRepository {
    List<Chapter> getFiles(Path diretorioDosMD);

}

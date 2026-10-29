package br.com.unipds;

import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.List;
import java.util.stream.Stream;

@ApplicationScoped
public class MarkDownRepositoryDirectory implements MarkDownRepository {

    @Override
    public List<Chapter> getFiles(Path diretorioDosMD) {
        try (Stream<Path> streamMDs = Files.list(diretorioDosMD)) {
            PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**/*.md");
            List<Path> arquivosMD = streamMDs
                    .filter(matcher::matches)
                    .sorted()
                    .toList();

            if (arquivosMD.isEmpty()) {
                throw new IllegalStateException("Não foram encontrados capítulos (arquivos .md) no diretório: " + diretorioDosMD.toAbsolutePath());
            }
            return arquivosMD.stream().map(arquivoMD -> {
                try {
                    Chapter chapter = new Chapter();
                    String markdown = Files.readString(arquivoMD);

                    chapter.setMarkdownContent(markdown);
                    chapter.setMarkdownFilePath(arquivoMD);
                    return chapter;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).toList();

            } catch(IOException ex){
                throw new IllegalStateException("Erro tentando encontrar arquivos .md em " + diretorioDosMD.toAbsolutePath(), ex);
            }
        }
    }

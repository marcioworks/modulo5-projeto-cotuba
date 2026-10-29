package br.com.unipds;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.Heading;
import org.commonmark.node.Node;
import org.commonmark.node.Text;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@ApplicationScoped
public class MarkdownReaderCommonMark implements MarkdownReader {

    @Override
    public void readFiles(List<Chapter> chapters) throws IOException {

        chapters.forEach(chapter -> {
            Parser parser = Parser.builder().build();
            Node document = null;
                String markdown = chapter.getMarkdownContent();
                document = parser.parse(markdown);
                document.accept(new AbstractVisitor() {
                    @Override
                    public void visit(Heading heading) {
                        if (heading.getLevel() == 1) {
                            // capítulo
                            String tituloDoCapitulo = ((Text) heading.getFirstChild()).getLiteral();
                            chapter.setTitle(tituloDoCapitulo);
                            // TODO: usar título do capítulo
                        } else if (heading.getLevel() == 2) {
                            // seção
                        } else if (heading.getLevel() == 3) {
                            // título
                        }
                    }

                });
                HtmlRenderer renderer = HtmlRenderer.builder().build();
                String html = renderer.render(document);
                chapter.setHtmlContent(html);
        });
    }
}
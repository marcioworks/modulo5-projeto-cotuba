package br.com.unipds;

import jakarta.enterprise.context.ApplicationScoped;
import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.Heading;
import org.commonmark.node.Node;
import org.commonmark.node.Text;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.io.IOException;
import java.util.List;

@ApplicationScoped
public class MarkdownReaderCommonMark implements MarkdownReader {

    @Override
    public List<Chapter> readFiles(List<Markdown> markdowns) throws IOException {

        return markdowns.stream()
                .map(markdown -> {
                    var chapterBuilder = ChapterBuilder.builder();
                    Parser parser = Parser.builder().build();
                    Node document = null;
                    chapterBuilder.markdown(markdown);
                    document = parser.parse(markdown.markdownContent());
                    document.accept(new AbstractVisitor() {
                        @Override
                        public void visit(Heading heading) {
                            if (heading.getLevel() == 1) {
                                // capítulo
                                String tituloDoCapitulo = ((Text) heading.getFirstChild()).getLiteral();
                                chapterBuilder.title(tituloDoCapitulo);
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
                    chapterBuilder.htmlContent(html);
                    return chapterBuilder.build();
                }).toList();
    }
}
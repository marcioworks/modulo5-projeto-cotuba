package br.com.unipds;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfOutline;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.navigation.PdfExplicitDestination;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.IElement;
import com.itextpdf.layout.properties.AreaBreakType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jdk.jfr.Name;

import java.nio.file.Files;
import java.util.List;

@ApplicationScoped
@Named("generatePDF")
public class GeneratePDF implements GenerateEbook {

    @Override
    public void generate(Ebook ebook) {
        try (var writer = new PdfWriter(Files.newOutputStream(ebook.getFilePath()));
             var pdf = new PdfDocument(writer);
             var pdfDocument = new Document(pdf)) {

            pdf.getDocumentInfo().setTitle(ebook.getTitle());
            pdf.getDocumentInfo().setAuthor(ebook.getAuthor());
            List<Chapter> chapters = ebook.getChapters();
            chapters.forEach(chapter -> {
                List<IElement> convertToElements = HtmlConverter.convertToElements(chapter.getHtmlContent());

                if (pdf.getNumberOfPages() == 0) {
                    pdf.addNewPage();
                }
                PdfOutline rootOutline = pdf.getOutlines(false);
                if (rootOutline == null) {
                    pdf.initializeOutlines();
                    rootOutline = pdf.getOutlines(false);
                }

                PdfOutline chapterOutline = rootOutline.addOutline(chapter.getTitle());
                chapterOutline.addDestination(PdfExplicitDestination.createFit(pdf.getLastPage()));

                for (IElement element : convertToElements) {
                    pdfDocument.add((IBlockElement) element);
                }
                // TODO: não adicionar página depois do último capítulo
                pdfDocument.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

            });

        } catch (
                Exception ex) {
            throw new IllegalStateException("Erro ao gerar PDF: " + ebook.getFilePath().toAbsolutePath(), ex);
        }
    }
}

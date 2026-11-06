package br.com.unipds;

import jakarta.enterprise.util.AnnotationLiteral;

public class EbookFormatFiler extends AnnotationLiteral<EbookFormatQualifier> implements EbookFormatQualifier {

    private final EbookExtension extension;

    public EbookFormatFiler(EbookExtension extension) {
        this.extension = extension;
    }

    @Override
    public EbookExtension value() {
        return extension;
    }

    public static EbookFormatFiler of(EbookExtension extension){
        return new EbookFormatFiler(extension);
    }

}

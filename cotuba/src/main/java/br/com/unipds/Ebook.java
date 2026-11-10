package br.com.unipds;

import io.soabase.recordbuilder.core.RecordBuilder;

import java.nio.file.Path;
import java.util.List;

@RecordBuilder
public record Ebook (
     String title,
     String author,
     List<Chapter> chapters,
     EbookExtension format,
     Path filePath
){}

package br.com.unipds;


import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
public record Chapter(String title, String htmlContent, Markdown markdown) {
}


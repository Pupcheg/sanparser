package me.supcheg.sanparser.book;

import org.springframework.context.annotation.Configuration;

@Configuration
class BookConfiguration {
    AttributeBookWriterFactory attributeBookWriterFactory() {
        return XlsxAttributeBookWriter::new;
    }
}

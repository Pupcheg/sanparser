package me.supcheg.sanparser.book;

import me.supcheg.sanparser.book.attribute.AttributeBookWriterFactory;
import me.supcheg.sanparser.book.attribute.XlsxAttributeBookWriter;
import me.supcheg.sanparser.book.variant.VariantBookWriterFactory;
import me.supcheg.sanparser.book.variant.XlsxVariantBookWriter;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class BookConfiguration {
    @Bean
    AttributeBookWriterFactory attributeBookWriterFactory(ObjectFactory<XlsxAttributeBookWriter> attributeBookWriter) {
        return availablePropertiesForGroup -> {
            var instance = attributeBookWriter.getObject();
            instance.setAvailablePropertiesForGroup(availablePropertiesForGroup);
            return instance;
        };
    }

    @Bean
    VariantBookWriterFactory variantBookWriterFactory(ObjectFactory<XlsxVariantBookWriter> variantBookWriter) {
        return associationsAmount -> {
            var instance = variantBookWriter.getObject();
            instance.setVariantsAmount(associationsAmount);
            return instance;
        };
    }
}

package me.supcheg.sanparser.book.variant;

import jakarta.annotation.PostConstruct;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Scope("prototype")
@Component
public class XlsxVariantBookWriter implements VariantBookWriter {
    private static final int SANTECH_IDENTIFIER_CELL_INDEX = 0;
    private static final int VARIANTS_CELLS_DELTA = 1;

    private final Workbook workbook;
    @MonotonicNonNull
    private Sheet sheet;
    private int variantsAmount;

    @Value("${santech.identifier-translation-name}")
    private String santechIdentifierTranslationName;
    @Value("${variants.sheet-translation-name}")
    private String sheetTranslationName;
    @Value("${variants.indexed-variant-translation-name}")
    private String indexedVariantTranslationName;

    XlsxVariantBookWriter() {
        this.workbook = new XSSFWorkbook();
    }

    @PostConstruct
    void postConstruct() {
        this.sheet = workbook.createSheet(sheetTranslationName);
    }

    public void setVariantsAmount(int variantsAmount) {
        this.variantsAmount = variantsAmount;

        var header = nextRow();
        header.createCell(SANTECH_IDENTIFIER_CELL_INDEX)
                .setCellValue(santechIdentifierTranslationName);

        for (int i = 1; i <= variantsAmount; i++) {
            header.createCell(i)
                    .setCellValue(indexedVariantTranslationName.formatted(i));
        }
    }

    @Override
    public void append(VariantBookEntry entry) {
        var associations = entry.associations();
        if (associations.size() > variantsAmount) {
            throw new IllegalArgumentException(
                    "Associations amount (%d) is bigger than max value (%d)"
                            .formatted(associations.size(), variantsAmount)
            );
        }

        var row = nextRow();
        row.createCell(SANTECH_IDENTIFIER_CELL_INDEX)
                .setCellValue(entry.key().nomenclatureNumber());

        for (int i = 0; i < associations.size(); i++) {
            row.createCell(VARIANTS_CELLS_DELTA + i)
                    .setCellValue(associations.get(i).value());
        }
    }

    private Row nextRow() {
        synchronized (workbook) {
            return sheet.createRow(sheet.getLastRowNum() + 1);
        }
    }

    @Override
    public void save(Path path) throws IOException {
        try (var out = Files.newOutputStream(path)) {
            workbook.write(out);
        }
    }

    @Override
    public void close() throws IOException {
        workbook.close();
    }
}

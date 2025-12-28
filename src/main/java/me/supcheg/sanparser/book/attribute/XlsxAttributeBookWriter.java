package me.supcheg.sanparser.book.attribute;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Scope("prototype")
@Component
public class XlsxAttributeBookWriter implements AttributeBookWriter {
    private static final int SANTECH_IDENTIFIER_CELL_INDEX = 0;
    private static final int PROPERTIES_CELLS_DELTA = 1;

    private final XSSFWorkbook workbook;
    private Map<String, List<String>> propertiesByGroup;
    private Table<String, String, Integer> cellIndexByGroupAndKey;

    @Value("${santech.identifier-translation-name}")
    private String santechIdentifierTranslationName;

    XlsxAttributeBookWriter() {
        workbook = new XSSFWorkbook();

        propertiesByGroup = Map.of();
        cellIndexByGroupAndKey = ImmutableTable.of();
    }

    public void setAvailablePropertiesForGroup(List<CategoryWithProperties> availablePropertiesForGroup) {
        Map<String, List<String>> propertiesByGroup = new HashMap<>();
        Table<String, String, Integer> cellIndexByGroupAndKey = HashBasedTable.create();

        for (var entry : availablePropertiesForGroup) {
            String category = normalizeSheetName(entry.category().name());
            List<String> properties = entry.properties().stream().sorted().toList();

            propertiesByGroup.put(category, properties);

            for (int index = 0; index < properties.size(); index++) {
                cellIndexByGroupAndKey.put(category, properties.get(index), PROPERTIES_CELLS_DELTA + index);
            }
        }

        this.propertiesByGroup = Map.copyOf(propertiesByGroup);
        this.cellIndexByGroupAndKey = ImmutableTable.copyOf(cellIndexByGroupAndKey);
    }

    @Override
    public void append(AttributeBookEntry bookEntry) {
        var group = normalizeSheetName(bookEntry.category().name());
        var values = Map.copyOf(bookEntry.properties());

        Sheet sheet = sheet(group);
        Row row = nextRow(sheet);

        row.createCell(SANTECH_IDENTIFIER_CELL_INDEX)
                .setCellValue(bookEntry.item().nomenclatureNumber());

        for (var entry : values.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            @Nullable Integer cellIndex = cellIndexByGroupAndKey.get(group, key);
            if (cellIndex == null) {
                throw new IllegalStateException("Not found cell index for group=%s and key=%s".formatted(group, key));
            }

            row.createCell(cellIndex)
                    .setCellValue(value);
        }
    }

    private String normalizeSheetName(String sheetname) {
        if (sheetname.length() > Workbook.MAX_SENSITIVE_SHEET_NAME_LEN) {
            return sheetname.substring(0, Workbook.MAX_SENSITIVE_SHEET_NAME_LEN);
        }

        return sheetname;
    }

    private Row nextRow(Sheet sheet) {
        synchronized (workbook) {
            return sheet.createRow(sheet.getLastRowNum() + 1);
        }
    }

    private Sheet sheet(String group) {
        synchronized (workbook) {
            Sheet sheet = workbook.getSheet(group);
            if (sheet == null) {
                sheet = workbook.createSheet(group);
                writeSheetHeader(sheet);
            }
            return sheet;
        }
    }

    private void writeSheetHeader(Sheet sheet) {
        List<String> properties = propertiesByGroup.get(sheet.getSheetName());
        if (properties == null) {
            throw new IllegalStateException("Not found properties for sheet=%s".formatted(sheet.getSheetName()));
        }

        Row header = sheet.createRow(0);

        header.getCell(SANTECH_IDENTIFIER_CELL_INDEX)
                .setCellValue(santechIdentifierTranslationName);

        for (int i = 0; i < properties.size(); i++) {
            header.createCell(PROPERTIES_CELLS_DELTA + i)
                    .setCellValue(properties.get(i));
        }
    }

    @Override
    public void save(Path outPath) throws IOException {
        try (var out = Files.newOutputStream(outPath)) {
            workbook.write(out);
        }
    }

    @Override
    public void close() throws IOException {
        workbook.close();
    }
}

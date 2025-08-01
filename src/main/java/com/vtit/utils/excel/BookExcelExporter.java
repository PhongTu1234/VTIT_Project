package com.vtit.utils.excel;

import com.vtit.entity.Book;
import com.vtit.entity.Category;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BookExcelExporter {

    public static ByteArrayInputStream exportToExcel(List<Book> books) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Books");
            int rowIdx = 0;

            // Header
            Row headerRow = sheet.createRow(rowIdx++);
            String[] headers = {"ID", "Tên sách", "Tác giả", "Số lượng", "Mô tả", "Loại sách"};

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // Data
            for (Book book : books) {
                Row row = sheet.createRow(rowIdx++);

                createCell(row, 0, book.getId());
                createCell(row, 1, book.getTitle());
                createCell(row, 2, book.getAuthor());
                createCell(row, 3, book.getQuantity());
                createCell(row, 4, book.getDescription());

                // Xử lý Category: book.getCategories() trả về Set<Category>
                String categoryNames = "";
                if (book.getCategory() != null && !book.getCategory().isEmpty()) {
                    categoryNames = book.getCategory()
                            .stream()
                            .map(Category::getName)
                            .collect(Collectors.joining(", "));
                }

                createCell(row, 5, categoryNames);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    private static void createCell(Row row, int columnIndex, Object value) {
        Cell cell = row.createCell(columnIndex);
        if (value == null) {
            cell.setBlank();
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else {
            cell.setCellValue(value.toString());
        }
    }
}

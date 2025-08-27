package com.vtit.utils.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.vtit.entity.Book;
import com.vtit.entity.Category;

public class BookExcelImporter {

    public static List<Book> importFromExcel(InputStream is) throws IOException {
        List<Book> books = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) { // bỏ header
                Row row = sheet.getRow(i);
                if (row == null || row.getCell(0) == null) continue;

                Book book = new Book();
                book.setTitle(row.getCell(1).getStringCellValue());
                book.setAuthor(row.getCell(2).getStringCellValue());
                book.setPublisher(row.getCell(3).getStringCellValue());
                book.setPageCount((int) row.getCell(4).getNumericCellValue());
                book.setLanguage(row.getCell(5).getStringCellValue());
                book.setQuantity((int) row.getCell(6).getNumericCellValue());

                // Validate dữ liệu nếu cần ở đây
                books.add(book);
            }
        }

        return books;
    }
}


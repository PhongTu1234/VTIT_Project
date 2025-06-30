package com.vtit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.vtit.service.BookService;

@SpringBootApplication
public class VtitProjectApplication implements CommandLineRunner {

    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(VtitProjectApplication.class, args);
    }

    @Override
    public void run(String... args) {
        // Gọi hàm đồng bộ ở đây
        bookService.syncBooksFromGoogleApi();
        System.out.println("Đã đồng bộ sách từ Google API");
    }
}


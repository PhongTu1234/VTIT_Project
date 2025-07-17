package com.vtit.dto.response.borrowing;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BorrowingDTO {
    private Integer id;
    private Integer userId;
    private Integer bookId;
    private Date borrowDate;
    private Date returnDate;
}

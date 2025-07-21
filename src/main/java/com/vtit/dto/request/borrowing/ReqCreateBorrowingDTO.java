package com.vtit.dto.request.borrowing;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqCreateBorrowingDTO {
    private Integer userId;
    private Integer bookId;
    private Date borrowDate;
    private Date returnDate;
}

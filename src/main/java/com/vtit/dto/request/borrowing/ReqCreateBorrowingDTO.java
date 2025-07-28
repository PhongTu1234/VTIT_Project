package com.vtit.dto.request.borrowing;

import com.vtit.dto.response.book.ResBookIdDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqCreateBorrowingDTO {
    private ResBookIdDTO book;
    private Integer borrow_duration;
}

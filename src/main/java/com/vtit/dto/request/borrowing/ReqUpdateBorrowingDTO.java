package com.vtit.dto.request.borrowing;

import java.time.Instant;

import com.vtit.dto.response.book.ResBookIdDTO;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ReqUpdateBorrowingDTO {
	private Integer id;
    private ResBookIdDTO book;
    private Integer borrowDuration;
    private Instant borrowDate;
    private Instant returnDate;
}
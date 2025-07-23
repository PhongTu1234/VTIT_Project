package com.vtit.dto.response.borrowing;

import java.time.Instant;

import com.vtit.dto.response.User.ResUserSummartDTO;
import com.vtit.dto.response.book.ResBookSummaryDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResUpdateBorrowingDTO {
	private Integer id;
	private ResUserSummartDTO user;
    private ResBookSummaryDTO book;
    private Integer borrowDuration;
    private Instant borrowDate;
    private Instant returnDate;
    private String updatedBy;
    private Instant updatedDate;
}

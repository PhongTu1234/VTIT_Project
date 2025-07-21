package com.vtit.dto.response.borrowing;

import java.time.Instant;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ResCreateBorrowingDTO {
	private Integer id;
	private Integer userId;
    private Integer bookId;
    private Date borrowDate;
    private Date returnDate;
    private String createdBy;
    private Instant createdDate;
}


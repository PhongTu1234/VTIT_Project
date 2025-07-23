package com.vtit.dto.response.book;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResBookSummaryDTO {
	private Integer id;
	private String title;
	private String author;
	private String publisher;
	private Instant publishedDate;
}

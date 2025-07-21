package com.vtit.dto.book;

import java.time.Instant;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqUpdateBookDTO {
	private Integer id;
	private String title;
	private String author;
	private String publisher;
	private Instant publishedDate;
	private Integer pageCount;
	private Integer quantity;
	private String printType;
	private String description;
	private String imageLink;
	private String language;
	
	private List<Integer> categoryIds;
}

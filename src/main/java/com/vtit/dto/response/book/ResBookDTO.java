package com.vtit.dto.response.book;

import java.time.Instant;
import java.util.List;

import com.vtit.dto.response.category.ResCategoryDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResBookDTO {
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
	private String createdBy;
	private Instant createdDate;
	private String updatedBy;
	private Instant updatedDate;
	
	private List<ResCategoryDTO> categories;
}

package com.vtit.mapper;

import java.time.Instant;

import org.springframework.stereotype.Component;

import com.vtit.dto.request.borrowing.ReqCreateBorrowingDTO;
import com.vtit.dto.request.borrowing.ReqUpdateBorrowingDTO;
import com.vtit.dto.response.User.ResUserSummartDTO;
import com.vtit.dto.response.book.ResBookSummaryDTO;
import com.vtit.dto.response.borrowing.ResBorrowingDTO;
import com.vtit.dto.response.borrowing.ResCreateBorrowingDTO;
import com.vtit.dto.response.borrowing.ResUpdateBorrowingDTO;
import com.vtit.entity.Borrowing;
import com.vtit.exception.ResourceNotFoundException;
import com.vtit.reponsitory.BookRepository;
import com.vtit.reponsitory.UserRepository;
import com.vtit.utils.SecurityUtil;

@Component
public class BorrowingMapper {
	
	private final UserRepository userRepository;
	private final SecurityUtil securityUtil;
	private final BookRepository bookRepository;
	
	public BorrowingMapper(UserRepository userRepository, SecurityUtil securityUtil, BookRepository bookRepository) {
		this.userRepository = userRepository;
		this.securityUtil = securityUtil;
		this.bookRepository = bookRepository;
	}
	
	public ResBorrowingDTO convertToResBorrowingDTO(Borrowing borrowing) {
		ResBorrowingDTO dto = new ResBorrowingDTO();
		dto.setId(borrowing.getId());

		ResUserSummartDTO userSummary = new ResUserSummartDTO();
		userSummary.setFullname(borrowing.getUser().getFullname());
		dto.setUser(userSummary);

		ResBookSummaryDTO bookSummary = new ResBookSummaryDTO();
		bookSummary.setId(borrowing.getId());
		bookSummary.setAuthor(borrowing.getBook().getAuthor());
		bookSummary.setTitle(borrowing.getBook().getTitle());
		bookSummary.setPublisher(borrowing.getBook().getPublisher());
		bookSummary.setPublishedDate(borrowing.getBook().getPublishedDate());
		dto.setBook(bookSummary);

		dto.setBorrowDate(borrowing.getBorrowDate());
		dto.setReturnDate(borrowing.getReturnDate());
		dto.setCreatedBy(borrowing.getCreatedBy());
		dto.setCreatedDate(borrowing.getCreatedDate());
		dto.setUpdatedBy(borrowing.getUpdatedBy());
		dto.setUpdatedDate(borrowing.getUpdatedDate());
		dto.setBorrowDuration(borrowing.getBorrowDuration());
		dto.setStatus(borrowing.getStatus());
		return dto;
	}

	public ResCreateBorrowingDTO convertToResCreateBorrowingDTO(Borrowing borrowing) {
		ResCreateBorrowingDTO dto = new ResCreateBorrowingDTO();
		dto.setId(borrowing.getId());

		ResUserSummartDTO userSummary = new ResUserSummartDTO();
		userSummary.setFullname(borrowing.getUser().getFullname());
		dto.setUser(userSummary);

		ResBookSummaryDTO bookSummary = new ResBookSummaryDTO();
		bookSummary.setId(borrowing.getId());
		bookSummary.setAuthor(borrowing.getBook().getAuthor());
		bookSummary.setTitle(borrowing.getBook().getTitle());
		bookSummary.setPublisher(borrowing.getBook().getPublisher());
		bookSummary.setPublishedDate(borrowing.getBook().getPublishedDate());
		dto.setBook(bookSummary);

		dto.setBorrowDate(borrowing.getBorrowDate());
		dto.setReturnDate(borrowing.getReturnDate());
		dto.setCreatedBy(borrowing.getCreatedBy());
		dto.setCreatedDate(borrowing.getCreatedDate());
		dto.setBorrowDuration(borrowing.getBorrowDuration());
		return dto;
	}

	public ResUpdateBorrowingDTO convertToResUpdateBorrowingDTO(Borrowing borrowing) {
		ResUpdateBorrowingDTO dto = new ResUpdateBorrowingDTO();
		dto.setId(borrowing.getId());

		ResUserSummartDTO userSummary = new ResUserSummartDTO();
		userSummary.setFullname(borrowing.getUser().getFullname());
		dto.setUser(userSummary);

		ResBookSummaryDTO bookSummary = new ResBookSummaryDTO();
		bookSummary.setId(borrowing.getId());
		bookSummary.setAuthor(borrowing.getBook().getAuthor());
		bookSummary.setTitle(borrowing.getBook().getTitle());
		bookSummary.setPublisher(borrowing.getBook().getPublisher());
		bookSummary.setPublishedDate(borrowing.getBook().getPublishedDate());
		dto.setBook(bookSummary);

		dto.setBorrowDate(borrowing.getBorrowDate());
		dto.setReturnDate(borrowing.getReturnDate());
		dto.setUpdatedBy(borrowing.getUpdatedBy());
		dto.setUpdatedDate(borrowing.getUpdatedDate());
		dto.setBorrowDuration(borrowing.getBorrowDuration());
		return dto;
	}

	public Borrowing convertToEntity(ReqCreateBorrowingDTO dto) {
		Borrowing borrowing = new Borrowing();
		borrowing.setUser(userRepository.findByUsername(securityUtil.getCurrentUserLogin().get()));
		borrowing.setBook(bookRepository.findById(dto.getBook().getBookId())
				.orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + dto.getBook().getBookId())));
		borrowing.setBorrowDuration(dto.getBorrow_duration());
		borrowing.setBorrowDate(Instant.now());
		return borrowing;
	}

	public Borrowing convertToEntity(Borrowing borrowing, ReqUpdateBorrowingDTO dto) {
		if (dto.getBook() != null) {
			borrowing.setBook(bookRepository.findById(dto.getBook().getBookId())
					.orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + dto.getBook().getBookId())));
		}
		if (dto.getBorrowDate() != null) borrowing.setBorrowDate(dto.getBorrowDate());
		if (dto.getReturnDate() != null) borrowing.setReturnDate(dto.getReturnDate());
		if (dto.getReturnDate() != null) borrowing.setReturnDate(dto.getReturnDate());
		if (dto.getBorrowDuration() != null) borrowing.setBorrowDuration(dto.getBorrowDuration());
		return borrowing;
	}
}

package com.vtit.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.vtit.dto.common.ResultPaginationDTO;
import com.vtit.dto.request.borrowing.ReqCreateBorrowingDTO;
import com.vtit.dto.request.borrowing.ReqUpdateBorrowingDTO;
import com.vtit.dto.response.User.ResUserSummartDTO;
import com.vtit.dto.response.book.ResBookSummaryDTO;
import com.vtit.dto.response.borrowing.ResBorrowingDTO;
import com.vtit.dto.response.borrowing.ResCreateBorrowingDTO;
import com.vtit.dto.response.borrowing.ResUpdateBorrowingDTO;
import com.vtit.entity.Book;
import com.vtit.entity.Borrowing;
import com.vtit.entity.Users;
import com.vtit.exception.ResourceNotFoundException;
import com.vtit.reponsitory.BookRepository;
import com.vtit.reponsitory.BorrowingRepository;
import com.vtit.reponsitory.UserRepository;
import com.vtit.service.BorrowingService;
import com.vtit.utils.IdValidator;
import com.vtit.utils.SecurityUtil;

import lombok.RequiredArgsConstructor;

@Service
public class BorrowingServiceImpl implements BorrowingService {

	private final BorrowingRepository borrowingRepository;
	private final UserRepository userRepository;
	private final BookRepository bookRepository;
	private final SecurityUtil securityUtil;

	public BorrowingServiceImpl(BorrowingRepository borrowingRepository, UserRepository userRepository,
			BookRepository bookRepository, SecurityUtil securityUtil) {
		this.borrowingRepository = borrowingRepository;
		this.userRepository = userRepository;
		this.bookRepository = bookRepository;
		this.securityUtil = securityUtil;
	}

	@Override
	public ResultPaginationDTO findAll(Specification<Borrowing> spec, Pageable pageable) {
		Page<Borrowing> pageBorrowing = borrowingRepository.findAll(spec, pageable);
		ResultPaginationDTO rs = new ResultPaginationDTO();
		ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

		mt.setPage(pageable.getPageNumber() + 1);
		mt.setPageSize(pageable.getPageSize());
		mt.setPages(pageBorrowing.getTotalPages());
		mt.setTotals((int) pageBorrowing.getTotalElements());

		rs.setMeta(mt);
		rs.setResult(pageBorrowing.getContent());
		return rs;
	}

	@Override
	public ResBorrowingDTO findById(String id) {
		Integer intId = IdValidator.validateAndParse(id);
		Borrowing borrowing = borrowingRepository.findById(intId)
				.orElseThrow(() -> new ResourceNotFoundException("Borrowing not found with id: " + id));
		return convertToResBorrowingDTO(borrowing);
	}

	@Override
	public ResCreateBorrowingDTO create(ReqCreateBorrowingDTO dto) {
		return convertToResCreateBorrowingDTO(borrowingRepository.save(convertToEntity(dto)));
	}

	@Override
	public ResUpdateBorrowingDTO update(ReqUpdateBorrowingDTO dto) {
		Borrowing b = borrowingRepository.findById(dto.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Borrowing not found with id: " + dto.getId()));
		return convertToResUpdateBorrowingDTO(borrowingRepository.save(convertToEntity(dto)));
	}

	@Override
	public void delete(String id) {
		Integer intId = IdValidator.validateAndParse(id);
		Borrowing borrowing = borrowingRepository.findById(intId)
				.orElseThrow(() -> new ResourceNotFoundException("Borrowing not found"));
		borrowingRepository.delete(borrowing);
	}

	public ResBorrowingDTO convertToResBorrowingDTO(Borrowing borrowing) {
		ResBorrowingDTO dto = new ResBorrowingDTO();
		dto.setId(borrowing.getId());

		ResUserSummartDTO userSummary = new ResUserSummartDTO();
		userSummary.setFullname(borrowing.getUser().getFullname());
		dto.setUser(userSummary);

		ResBookSummaryDTO bookSummary = new ResBookSummaryDTO();
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
		dto.setBorrowDuration(borrowing.getBorrowDuration()); // thêm dòng này nếu response có
		return dto;
	}

	public ResCreateBorrowingDTO convertToResCreateBorrowingDTO(Borrowing borrowing) {
		ResCreateBorrowingDTO dto = new ResCreateBorrowingDTO();
		dto.setId(borrowing.getId());

		ResUserSummartDTO userSummary = new ResUserSummartDTO();
		userSummary.setFullname(borrowing.getUser().getFullname());
		dto.setUser(userSummary);

		ResBookSummaryDTO bookSummary = new ResBookSummaryDTO();
		bookSummary.setAuthor(borrowing.getBook().getAuthor());
		bookSummary.setTitle(borrowing.getBook().getTitle());
		bookSummary.setPublisher(borrowing.getBook().getPublisher());
		bookSummary.setPublishedDate(borrowing.getBook().getPublishedDate());
		dto.setBook(bookSummary);

		dto.setBorrowDate(borrowing.getBorrowDate());
		dto.setReturnDate(borrowing.getReturnDate());
		dto.setCreatedBy(borrowing.getCreatedBy());
		dto.setCreatedDate(borrowing.getCreatedDate());
		dto.setBorrowDuration(borrowing.getBorrowDuration()); // thêm dòng này nếu response có
		return dto;
	}

	public ResUpdateBorrowingDTO convertToResUpdateBorrowingDTO(Borrowing borrowing) {
		ResUpdateBorrowingDTO dto = new ResUpdateBorrowingDTO();
		dto.setId(borrowing.getId());

		ResUserSummartDTO userSummary = new ResUserSummartDTO();
		userSummary.setFullname(borrowing.getUser().getFullname());
		dto.setUser(userSummary);

		ResBookSummaryDTO bookSummary = new ResBookSummaryDTO();
		bookSummary.setAuthor(borrowing.getBook().getAuthor());
		bookSummary.setTitle(borrowing.getBook().getTitle());
		bookSummary.setPublisher(borrowing.getBook().getPublisher());
		bookSummary.setPublishedDate(borrowing.getBook().getPublishedDate());
		dto.setBook(bookSummary);

		dto.setBorrowDate(borrowing.getBorrowDate());
		dto.setReturnDate(borrowing.getReturnDate());
		dto.setUpdatedBy(borrowing.getUpdatedBy());
		dto.setUpdatedDate(borrowing.getUpdatedDate());
		dto.setBorrowDuration(borrowing.getBorrowDuration()); // thêm dòng này nếu response có
		return dto;
	}

	public Borrowing convertToEntity(ReqCreateBorrowingDTO dto) {
		Borrowing borrowing = new Borrowing();
		borrowing.setUser(userRepository.findByEmail(securityUtil.getCurrentUserLogin().get()));
		borrowing.setBook(bookRepository.findById(dto.getBook().getBookId())
				.orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + dto.getBook().getBookId())));
		borrowing.setBorrowDate(dto.getBorrowDate());
		borrowing.setReturnDate(dto.getReturnDate());
		borrowing.setBorrowDuration(dto.getBorrowDuration()); // thêm dòng này
		return borrowing;
	}

	public Borrowing convertToEntity(ReqUpdateBorrowingDTO dto) {
		Borrowing borrowing = new Borrowing();
		borrowing.setId(dto.getId());
		borrowing.setBook(bookRepository.findById(dto.getBook().getBookId())
				.orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + dto.getBook().getBookId())));
		borrowing.setBorrowDate(dto.getBorrowDate());
		borrowing.setReturnDate(dto.getReturnDate());
		borrowing.setBorrowDuration(dto.getBorrowDuration()); // thêm dòng này
		return borrowing;
	}
}
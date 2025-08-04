package com.vtit.service.impl;

import java.time.Instant;
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
import com.vtit.dto.response.book.ResBookDTO;
import com.vtit.dto.response.book.ResBookSummaryDTO;
import com.vtit.dto.response.borrowing.ResBorrowingDTO;
import com.vtit.dto.response.borrowing.ResCreateBorrowingDTO;
import com.vtit.dto.response.borrowing.ResUpdateBorrowingDTO;
import com.vtit.entity.Book;
import com.vtit.entity.Borrowing;
import com.vtit.entity.Users;
import com.vtit.exception.ResourceNotFoundException;
import com.vtit.mapper.BorrowingMapper;
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
	private final BorrowingMapper borrowingMapper;

	public BorrowingServiceImpl(BorrowingRepository borrowingRepository, UserRepository userRepository,
			BookRepository bookRepository, SecurityUtil securityUtil, BorrowingMapper borrowingMapper) {
		this.borrowingRepository = borrowingRepository;
		this.userRepository = userRepository;
		this.bookRepository = bookRepository;
		this.securityUtil = securityUtil;
		this.borrowingMapper = borrowingMapper;
	}

	@Override
	public ResultPaginationDTO findAll(Specification<Borrowing> spec, Pageable pageable) {
		Page<Borrowing> pageBorrowing = borrowingRepository.findAll(spec, pageable);
		ResultPaginationDTO rs = new ResultPaginationDTO();
		ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
		List<ResBorrowingDTO> borrowingDTOs = pageBorrowing.getContent().stream()
				.map(borrowingMapper::convertToResBorrowingDTO).collect(Collectors.toList());

		mt.setPage(pageable.getPageNumber() + 1);
		mt.setPageSize(pageable.getPageSize());
		mt.setPages(pageBorrowing.getTotalPages());
		mt.setTotals((int) pageBorrowing.getTotalElements());

		rs.setMeta(mt);
		rs.setResult(borrowingDTOs);
		return rs;
	}

	@Override
	public ResBorrowingDTO findById(String id) {
		Integer intId = IdValidator.validateAndParse(id);
		Borrowing borrowing = borrowingRepository.findById(intId)
				.orElseThrow(() -> new ResourceNotFoundException("Borrowing not found with id: " + id));
		return borrowingMapper.convertToResBorrowingDTO(borrowing);
	}

	@Override
	public ResCreateBorrowingDTO create(ReqCreateBorrowingDTO dto) {
		return borrowingMapper
				.convertToResCreateBorrowingDTO(borrowingRepository.save(borrowingMapper.convertToEntity(dto)));
	}

	@Override
	public ResUpdateBorrowingDTO update(ReqUpdateBorrowingDTO dto) {
		Borrowing borrowing = borrowingRepository.findById(dto.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Borrowing not found with id: " + dto.getId()));
		return borrowingMapper.convertToResUpdateBorrowingDTO(
				borrowingRepository.save(borrowingMapper.convertToEntity(borrowing, dto)));
	}

	@Override
	public void delete(String id) {
		Integer intId = IdValidator.validateAndParse(id);
		Borrowing borrowing = borrowingRepository.findById(intId)
				.orElseThrow(() -> new ResourceNotFoundException("Borrowing not found"));
		borrowingRepository.delete(borrowing);
	}
}
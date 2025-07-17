package com.vtit.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.vtit.dto.common.ResultPaginationDTO;
import com.vtit.dto.response.borrowing.BorrowingDTO;
import com.vtit.entity.Book;
import com.vtit.entity.Borrowing;
import com.vtit.entity.Users;
import com.vtit.exception.ResourceNotFoundException;
import com.vtit.reponsitory.BookRepository;
import com.vtit.reponsitory.BorrowingRepository;
import com.vtit.reponsitory.UserRepository;
import com.vtit.service.BorrowingService;
import com.vtit.utils.IdValidator;

import lombok.RequiredArgsConstructor;

@Service
public class BorrowingServiceImpl implements BorrowingService {

    private final BorrowingRepository borrowingRepo;
    private final UserRepository userRepo;
    private final BookRepository bookRepo;
    
	public BorrowingServiceImpl(BorrowingRepository borrowingRepo, UserRepository userRepo, BookRepository bookRepo) {
		this.borrowingRepo = borrowingRepo;
		this.userRepo = userRepo;
		this.bookRepo = bookRepo;
	}

    private BorrowingDTO mapToDTO(Borrowing b) {
        BorrowingDTO dto = new BorrowingDTO();
        dto.setId(b.getId());
        dto.setUserId(b.getUser().getId());
        dto.setBookId(b.getBook().getId());
        dto.setBorrowDate(b.getBorrowDate());
        dto.setReturnDate(b.getReturnDate());
        return dto;
    }

    @Override
    public ResultPaginationDTO findAll(Specification<Borrowing> spec, Pageable pageable) {
    	Page<Borrowing> pageBorrowing = borrowingRepo.findAll(spec, pageable);
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
    public BorrowingDTO findById(Integer id) {
        Borrowing borrowing = borrowingRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Borrowing not found with id: " + id));
        return mapToDTO(borrowing);
    }

    @Override
    public BorrowingDTO create(BorrowingDTO dto) {
        Users user = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Book book = bookRepo.findById(dto.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        Borrowing b = new Borrowing();
        b.setUser(user);
        b.setBook(book);
        b.setBorrowDate(dto.getBorrowDate());
        b.setReturnDate(dto.getReturnDate());

        return mapToDTO(borrowingRepo.save(b));
    }

    @Override
    public BorrowingDTO update(Integer id, BorrowingDTO dto) {
        Borrowing b = borrowingRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Borrowing not found"));

        if (dto.getUserId() != null) {
            Users user = userRepo.findById(dto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            b.setUser(user);
        }

        if (dto.getBookId() != null) {
            Book book = bookRepo.findById(dto.getBookId())
                    .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
            b.setBook(book);
        }

        b.setBorrowDate(dto.getBorrowDate());
        b.setReturnDate(dto.getReturnDate());

        return mapToDTO(borrowingRepo.save(b));
    }

    @Override
    public void delete(String id) {
    	Integer intId = IdValidator.validateAndParse(id);
        Borrowing borrowing = borrowingRepo.findById(intId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrowing not found"));
        borrowingRepo.delete(borrowing);
    }
}

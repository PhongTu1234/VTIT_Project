package com.vtit.service.impl;

import org.springframework.stereotype.Service;

import com.vtit.reponsitory.BorrowingRepository;
import com.vtit.service.BorrowingService;

@Service
public class BorrowingServiceImpl implements BorrowingService{
	private final BorrowingRepository BorrowingRepository;
	
	public BorrowingServiceImpl(BorrowingRepository BorrowingRepository) {
		this.BorrowingRepository = BorrowingRepository;
	}
}

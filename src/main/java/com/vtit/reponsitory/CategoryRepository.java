package com.vtit.reponsitory;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vtit.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
	Optional<Category> findByName(String name);
}

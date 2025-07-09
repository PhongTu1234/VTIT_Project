package com.vtit.reponsitory;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vtit.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
	Optional<Category> findByName(String name);
	boolean existsByName(String name);
	boolean existsByCode(String code);
	boolean existsByNameAndIdNot(String name, Integer id);
	boolean existsByCodeAndIdNot(String code, Integer id);
}

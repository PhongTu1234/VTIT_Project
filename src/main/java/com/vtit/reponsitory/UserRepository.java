package com.vtit.reponsitory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vtit.entity.Users;

public interface UserRepository extends JpaRepository<Users, Integer> {

}

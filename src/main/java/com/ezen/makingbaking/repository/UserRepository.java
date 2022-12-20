package com.ezen.makingbaking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezen.makingbaking.entity.User;

public interface UserRepository extends JpaRepository<User, String> {

}

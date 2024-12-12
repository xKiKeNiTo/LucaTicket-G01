package com.grupo01.spring.repository;

import com.grupo01.spring.model.User;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<User, String> {
	boolean existsById(String mail);
}

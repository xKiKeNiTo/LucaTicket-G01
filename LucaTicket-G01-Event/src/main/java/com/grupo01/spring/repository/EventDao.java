package com.grupo01.spring.repository;

import com.grupo01.spring.model.Event;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventDao extends JpaRepository<Event, UUID> {
	// Métodos personalizados si fuese necesario
}

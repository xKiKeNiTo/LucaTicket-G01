package com.grupo01.spring.repository;

import com.grupo01.spring.model.Event;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventDao extends JpaRepository<Event, UUID> {
	
//	Nombre exacto
//	List<Event> findByNombre(String nombre);
	
	List<Event> findByNombreContainingIgnoreCase(String nombre);
}

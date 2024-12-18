package com.grupo01.spring.repository;

import com.grupo01.spring.model.Event;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EventDao extends JpaRepository<Event, UUID> {
	
//	Nombre exacto
//	List<Event> findByNombre(String nombre);
	
	List<Event> findByNombreContainingIgnoreCase(String nombre);
	
	@Modifying
    @Query("UPDATE Event e SET e.precioMinimo = :precioMinimo, e.precioMaximo = :precioMaximo WHERE e.id = :idEvento")
    void actualizarPrecios(@Param("idEvento") UUID idEvento,
                           @Param("precioMinimo") BigDecimal precioMinimo,
                           @Param("precioMaximo") BigDecimal precioMaximo);
	
}

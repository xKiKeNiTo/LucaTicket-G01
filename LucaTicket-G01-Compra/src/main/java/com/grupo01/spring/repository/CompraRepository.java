package com.grupo01.spring.repository;

import com.grupo01.spring.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface CompraRepository extends JpaRepository<Compra, UUID> {
    boolean existsByUserMailAndIdEvent(String userMail, UUID idEvent);
	
	List<Compra> findAllByUserMail(String mail);

    @Query("SELECT DISTINCT c.idEvent FROM Compra c")
    List<UUID> findDistinctIdEventos(); // Devuelve IDs únicos de eventos con compras

    @Query("SELECT AVG(c.precio) FROM Compra c WHERE c.idEvent = :evento")
    BigDecimal calcularPrecioPromedioPorEvento(@Param("evento") UUID idEvent);

    @Query("SELECT MAX(c.precio) FROM Compra c WHERE c.idEvent = :evento")
    BigDecimal findMaxPrecioPorEvento(@Param("evento") UUID idEvent);

    @Query("SELECT MIN(c.precio) FROM Compra c WHERE c.idEvent = :evento")
    BigDecimal findMinPrecioPorEvento(@Param("evento") UUID idEvent);

    @Query("SELECT COUNT(c.precio) FROM Compra c WHERE c.idEvent = :evento")
    BigDecimal contarComprasPorEvento(@Param("evento") UUID idEvent);
}

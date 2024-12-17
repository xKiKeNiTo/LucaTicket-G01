package com.grupo01.spring.repository;

import com.grupo01.spring.model.HistoricoVentas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
public interface HistoricoVentasRepository extends JpaRepository<HistoricoVentas, UUID> {

    @Query("SELECT AVG(h.precioMedio) FROM HistoricoVentas h WHERE h.idEvent = :evento")
    BigDecimal calcularPrecioPromedioPorEvento(@Param("evento") UUID idEvent);
}

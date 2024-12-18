package com.grupo01.spring.repository;

import com.grupo01.spring.model.HistoricoVentas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HistoricoVentasRepository extends JpaRepository<HistoricoVentas, UUID> {

    // Buscar un registro por idEvento
    HistoricoVentas findByIdEvento(UUID idEvento);
}


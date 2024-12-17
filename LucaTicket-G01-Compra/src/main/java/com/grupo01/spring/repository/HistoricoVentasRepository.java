package com.grupo01.spring.repository;


import com.grupo01.spring.model.HistoricoVentas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HistoricoVentasRepository extends JpaRepository<HistoricoVentas, UUID> {

}

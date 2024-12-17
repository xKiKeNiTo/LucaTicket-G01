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

    @Query("SELECT AVG(o.precio) FROM order_history o WHERE o.id_evento = :idEvent")
    BigDecimal calcularPrecioPromedioPorEvento(@Param("idEvento") UUID idEvent);
}

package com.grupo01.spring.repository;

import com.grupo01.spring.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CompraRepository extends JpaRepository<Compra, UUID> {
    boolean existsByUserMailAndIdEvent(String userMail, UUID idEvent);
}

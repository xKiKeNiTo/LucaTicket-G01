package com.grupo01.spring.service;

import com.grupo01.spring.model.HistoricoVentas;
import com.grupo01.spring.repository.CompraRepository;
import com.grupo01.spring.repository.HistoricoVentasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class BatchServiceImpl implements BatchService {

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private HistoricoVentasRepository historicoVentasRepository;

    @Override
    public void procesarDatosVenta() {
        // Obtener todos los eventos únicos con compras
        List<UUID> eventos = compraRepository.findDistinctIdEventos();

        for (UUID idEvento : eventos) {
            // Calcular el precio medio para el evento
            BigDecimal precioMedio = compraRepository.calcularPrecioPromedioPorEvento(idEvento);

            if (precioMedio != null) {
                // Verificar si ya existe un registro para este evento
                HistoricoVentas historicoExistente = historicoVentasRepository.findByIdEvento(idEvento);

                if (historicoExistente != null) {
                    // Actualizar el registro existente
                    historicoExistente.setPrecioMedio(precioMedio);
                    historicoExistente.setTimestamp(LocalDateTime.now());
                    historicoVentasRepository.save(historicoExistente);
                } else {
                    // Crear un nuevo registro
                    HistoricoVentas nuevoHistorico = new HistoricoVentas();
                    nuevoHistorico.setIdEvento(idEvento);
                    nuevoHistorico.setPrecioMedio(precioMedio);
                    nuevoHistorico.setTimestamp(LocalDateTime.now());
                    historicoVentasRepository.save(nuevoHistorico);
                }
            }
        }
    }
}


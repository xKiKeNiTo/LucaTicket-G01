package com.grupo01.spring.config;

import com.grupo01.spring.service.BatchServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledConfig {

    @Autowired
    private BatchServiceImpl batchServiceImpl;

    @Scheduled(cron = "0 0 1 * * ?") // Ejecución diaria a la 1 AM
    public void ejecutarProcesoBatch() {
    	batchServiceImpl.procesarDatosVenta();
    }
}

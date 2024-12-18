package com.grupo01.spring.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledConfig {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job jobVentaHistorico;

    @Autowired
    private Job jobActualizarEventos;

    @Scheduled(cron = "0 0 1 * * ?") // Proceso diario a las 1:00 AM
    public void ejecutarHistorico() {
        try {
            jobLauncher.run(jobVentaHistorico, new JobParameters());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0 30 1 * * ?") // Proceso diario a la 1:30 AM
    public void ejecutarActualizarEventos() {
        try {
            jobLauncher.run(jobActualizarEventos, new JobParameters());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


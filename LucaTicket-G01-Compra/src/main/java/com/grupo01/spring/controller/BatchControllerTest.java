package com.grupo01.spring.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/batch")
public class BatchControllerTest {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job jobVentaHistorico;

    @Autowired
    private Job jobActualizarEventos;

    @GetMapping("/runVentaHistorico")
    public ResponseEntity<String> ejecutarVentaHistorico() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis()) // Evitar que el Job sea considerado duplicado
                    .toJobParameters();
            jobLauncher.run(jobVentaHistorico, jobParameters);
            return ResponseEntity.ok("Proceso VentaHistorico ejecutado correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error ejecutando el proceso VentaHistorico.");
        }
    }

    @GetMapping("/runActualizarEventos")
    public ResponseEntity<String> ejecutarActualizarEventos() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis()) // Evitar que el Job sea considerado duplicado
                    .toJobParameters();
            jobLauncher.run(jobActualizarEventos, jobParameters);
            return ResponseEntity.ok("Proceso ActualizarEventos ejecutado correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error ejecutando el proceso ActualizarEventos.");
        }
    }
}

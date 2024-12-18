package com.grupo01.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grupo01.spring.service.BatchService;

@RestController
@RequestMapping("batch")
public class BatchControllerTest {

	@Autowired
    private BatchService batchService;

    @GetMapping("/run")
    public String runBatch() {
    	batchService.procesarDatosVenta();
        return "Proceso batch ejecutado";
    }
	
}

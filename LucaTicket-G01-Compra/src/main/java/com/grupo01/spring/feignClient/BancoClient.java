package com.grupo01.spring.feignClient;

import com.grupo01.spring.model.BancoRequest;
import com.grupo01.spring.model.BancoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "banco-service", url = "http://banco.eu-west-3.elasticbeanstalk.com/pasarela")
public interface BancoClient {

	@PostMapping("/validaruser")
	Map<String, String> autenticarUsuario(@RequestParam Map<String, String> credentials);

	@PostMapping("/validacion")
	BancoResponse validarCompra(@RequestBody BancoRequest bancoRequest, @RequestHeader("Authorization") String token);
}

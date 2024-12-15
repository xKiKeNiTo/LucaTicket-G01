package com.grupo01.spring.feignClient;

import com.grupo01.spring.model.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service")
public interface UserClient {

	@GetMapping("/users/findByMail")
	UserResponse getUserByEmail(@RequestParam("mail") String email);
}

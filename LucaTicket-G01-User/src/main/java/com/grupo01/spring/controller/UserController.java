package com.grupo01.spring.controller;

import com.grupo01.spring.model.UserRequest;
import com.grupo01.spring.model.UserResponse;
import com.grupo01.spring.service.UserService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

	/**
	 * Endpoint para crear un nuevo usuario
	 *
	 * @param userRequest datos del usuario a crear.
	 * @return ResponseEntity con UserResponse y el estado HTTP.
	 */
	@PostMapping("/save")
	public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {

		UserRequest userRequestDomain = new UserRequest(userRequest.getMail(), userRequest.getNombre(),
				userRequest.getApellido(), userRequest.getContrasena());

		UserResponse userResponseDomain = userService.save(userRequestDomain);

		UserResponse userResponse = new UserResponse(userResponseDomain.getMail(), userResponseDomain.getNombre(),
				userResponseDomain.getApellido(), userResponseDomain.getFechaAlta());

		return ResponseEntity.status(201).body(userResponse);
	}
	
	 @GetMapping("/findByMail")
	    public ResponseEntity<UserResponse> findByMail(@RequestParam("mail") @NotBlank @Email String mail) {
	        UserResponse userResponse = userService.findByMail(mail);
	        return ResponseEntity.ok(userResponse);
	    }

	
}

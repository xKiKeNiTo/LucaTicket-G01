package com.grupo01.spring.controller;

import com.grupo01.spring.model.UserRequest;
import com.grupo01.spring.model.UserResponse;
import com.grupo01.spring.service.UserServiceImpl;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    /**
     * Endpoint para crear un nuevo usuario
     *
     * @param userRequest datos del usuario a crear.
     * @return ResponseEntity con UserResponse y el estado HTTP.
     */
    @PostMapping("/save")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
       
        UserResponse createdUser = userServiceImpl.save(userRequest);

        UserResponse userResponse = new UserResponse(
                createdUser.getMail(),
                createdUser.getNombre(),
                createdUser.getApellido(),
                createdUser.getFechaAlta()
        );

        return ResponseEntity.status(201).body(userResponse);
    }
}

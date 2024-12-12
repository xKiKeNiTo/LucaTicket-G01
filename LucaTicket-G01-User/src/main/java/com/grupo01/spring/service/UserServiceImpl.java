package com.grupo01.spring.service;

import com.grupo01.spring.model.User;
import com.grupo01.spring.model.UserRequest;
import com.grupo01.spring.model.UserResponse;

public class UserServiceImpl implements UserService {

	UserResponse mapToResponse(User user) {
	    return new UserResponse(
	        user.getMail(),
	        user.getNombre(),
	        user.getApellido(),
	        user.getFechaAlta()
	    );
	}
	
	User mapToEntity(UserRequest request) {
		User event = new User();
		event.setMail(request.getMail());
		event.setNombre(request.getNombre());
		event.setApellido(request.getApellido());
		event.setContrasena(request.getContrasena());
		return event;
	}

}

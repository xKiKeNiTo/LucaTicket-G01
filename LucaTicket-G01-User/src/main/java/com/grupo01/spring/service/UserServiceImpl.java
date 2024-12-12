package com.grupo01.spring.service;

import com.grupo01.spring.model.User;
import com.grupo01.spring.model.UserRequest;
import com.grupo01.spring.model.UserResponse;
import com.grupo01.spring.repository.UserDao;
import jakarta.transaction.Transactional;

public class UserServiceImpl implements UserService {

	private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

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

	@Override
	@Transactional
	public UserResponse save(UserRequest userRequest) {
		User event = mapToEntity(userRequest);
		User savedEvent = userDao.save(event);
		return mapToResponse(savedEvent);
	}
}

package com.grupo01.spring.service;

import com.grupo01.spring.model.UserRequest;
import com.grupo01.spring.model.UserResponse;

public interface UserService {
    UserResponse save(UserRequest userRequest);

    UserResponse findByMail(String mail);
}

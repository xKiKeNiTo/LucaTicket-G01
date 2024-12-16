package com.grupo01.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.grupo01.spring.model.User;
import com.grupo01.spring.model.UserRequest;
import com.grupo01.spring.model.UserResponse;
import com.grupo01.spring.repository.UserDao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
	
	@Mock
	private UserDao userDao;
	
	@InjectMocks
	private UserServiceImpl userServiceImpl;
	

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	void debeCrearUsuario() {
	    // Crea el objeto UserRequest simulando la entrada del usuario
	    UserRequest request = new UserRequest();
	    request.setNombre("Nombre");
	    request.setApellido("Apellido");
	    request.setMail("mail@email.com"); 
	    request.setContrasena("contraseña");
	    
	    // Crea el objeto User que representa lo que guardará el DAO
	    User user = new User();
	    user.setNombre("Nombre");
	    user.setApellido("Apellido");
	    user.setMail("mail@email.com"); 
	    user.setContrasena("contraseña");
	    
	    // Configuración del mock para que devuelva el usuario simulado al guardar
	    when(userDao.save(any(User.class))).thenReturn(user);
	    
	    // Llama al método del servicio que estamos probando
	    UserResponse response = userServiceImpl.save(request);
	    
	    // Verificaciones
	    assertNotNull(response);
	    assertEquals("Nombre", response.getNombre());
	    assertEquals("Apellido", response.getApellido());
	    assertEquals("mail@email.com", response.getMail()); 
	}	

}

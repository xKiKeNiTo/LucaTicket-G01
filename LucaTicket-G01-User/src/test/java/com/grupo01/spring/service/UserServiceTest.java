package com.grupo01.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import com.grupo01.spring.model.User;
import com.grupo01.spring.model.UserRequest;
import com.grupo01.spring.model.UserResponse;
import com.grupo01.spring.repository.UserDao;

import java.time.LocalDate;
import java.util.Optional;

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

	@Test
	void debeDevolver404CuandoUsuarioNoExiste() {
	    // Configurar el mock para devolver vacío cuando se busca un usuario inexistente
	    String email = "nonexistent@mail.com";
	    when(userDao.findByMail(email)).thenReturn(Optional.empty());

	    // Verifica que se lanza una ResponseStatusException cuando el usuario no existe
	    Exception exception = assertThrows(ResponseStatusException.class, () -> userServiceImpl.findByMail(email));

	    // Verifica que el mensaje de la excepción sea el esperado
	    assertEquals("404 NOT_FOUND \"Usuario no encontrado con mail: nonexistent@mail.com\"", exception.getMessage());

	    // Verifica que el DAO fue llamado correctamente
	    verify(userDao).findByMail(email);
	}
	
	@Test
	void debeGuardarUsuarioConCamposValidos() {
	    // Configuración de entrada
	    UserRequest request = new UserRequest();
	    request.setMail("test@mail.com");
	    request.setNombre("John");
	    request.setApellido("Doe");
	    request.setContrasena("password123");

	    // Configuración del objeto User que se guarda en el DAO
	    User user = new User();
	    user.setMail("test@mail.com");
	    user.setNombre("John");
	    user.setApellido("Doe");
	    user.setContrasena("password123");
	    user.setFechaAlta(LocalDate.now());

	    // Configuración del mock para devolver el usuario simulado
	    when(userDao.save(any(User.class))).thenReturn(user);

	    // Llama al método del servicio
	    UserResponse response = userServiceImpl.save(request);

	    // Verificaciones de la respuesta
	    assertNotNull(response);
	    assertEquals("test@mail.com", response.getMail());
	    assertEquals("John", response.getNombre());
	    assertEquals("Doe", response.getApellido());
	    assertEquals(LocalDate.now(), response.getFechaAlta());

	    // Verifica que el DAO fue llamado con un objeto User
	    verify(userDao).save(any(User.class));
	}


}

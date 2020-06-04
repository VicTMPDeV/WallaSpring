package com.victormoralesperez.wallaspring.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.victormoralesperez.wallaspring.models.Usuario;
import com.victormoralesperez.wallaspring.repositories.IUsuarioRepositoryDAO;

//Indicamos que es uns ervicio de detalles de usuario
@Service("userDetailsService") // Es muy importante esta línea para decir que vamos a usar el servicio de usuarios Spring
public class UserDetailsServiceImpl implements UserDetailsService { // LAS CLASES QUE SIRVEN PARA IMPLEMENTAR UNA INTERFAZ LAS NOMBRAMOS CON EL NOMBRE DE LA INTERFAZ SEGUIDO DE LA PALABRA IMPL (de Implementación)

	@Autowired
	IUsuarioRepositoryDAO repositorio; // DONDE RECOGERE LOS USUARIOS DE LA BASE DE DATOS

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { // NOS OBLIGA A DARLE CUERPO A ESTE METODO DE BUSCAR UN USUARIO POR SU NOMBRE DE USUARIO Y DEVOLVER CON EL UNA INSTANCIA DE USER DETAILS PARA QUE EL PUEDA COMPLETAR EL PROCESO DE AUTENTICACIÓN

		// Buscamos el usuario
		Usuario usuario = repositorio.findByEmail(username); // EL USERNAME ES EL EMAIL PORQUE ASI LO HEMOS DECIDIDO

		// Construimos el builder los datos de acceso
		User.UserBuilder builder = null;

		if (usuario != null) {
			builder = User.withUsername(username); // USUARIO CON EMAIL...
			builder.disabled(false); // QUE NO ESTE DESHABILITADO
			builder.password(usuario.getPassword()); // CON PASSWORD...EL SUYO
			builder.authorities(new SimpleGrantedAuthority("ROLE_USER")); // PRIVILEGIOS DEL USUARIO...USAMOS ESTA CLASE
																			// QUE PROPORCIONA SPRING PARA NO CREAR LA
																			// NUESTRA
		} else {
			// Si no lo encontramos lanzamos excepción
			throw new UsernameNotFoundException("Usuario no encontrado");
		}

		return builder.build(); // Devolvemos el usuario contrsuido

	}
}

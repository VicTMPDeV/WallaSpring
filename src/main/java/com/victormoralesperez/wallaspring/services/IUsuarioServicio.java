package com.victormoralesperez.wallaspring.services;

import com.victormoralesperez.wallaspring.models.Usuario;

/**
 * INTERFAZ IUsuarioServicio
 * -------------------------------------------------------------------------------------------
 * Interfaz que Declara los Metodos de Servicio para solicitar acciones a los Repositorios.
 * Desarrollaremos la Implementacion de estos Metodos en las Clases que Implementan estas
 * Interfaces. Hacemos esto asi para encapsular el Código Fuente de estas Clases mencionadas
 * una vez compilado el proyecto y asi solo ofreceremos la Funcionalidad a traves del uso de 
 * esta Interfaz Publica.
 * 
 * @author Victor Morales Perez
 *
 */

public interface IUsuarioServicio {
	
	public Usuario registrar(Usuario user);
	public Usuario editar(Usuario user);
	public Usuario findById(long id);
	public Usuario buscarPorEMail(String email);

}

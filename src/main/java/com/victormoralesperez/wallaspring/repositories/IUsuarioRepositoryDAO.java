package com.victormoralesperez.wallaspring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.victormoralesperez.wallaspring.models.Usuario;

/**
 * INTERFAZ IUsuarioRepository
 * -------------------------------------------------------------------------------------------
 * Repositorio de Operaciones DAO (CRUD) asociadas a la Entidad USUARIO
 * 
 * @author Victor Morales Perez
 *
 */

public interface IUsuarioRepositoryDAO extends JpaRepository<Usuario, Long> {

	/*
	 * INFO:
	 * -----------------------------------------------------------------------------
	 * Ademas de los Metodos CRUD y de Paginacion proporcionados por JpaRepository,
	 * necesitamos implementar determinadas operaciones intrinsecas a la propia
	 * naturaleza de las entidades definidas en nuestro proyecto y su relacion con
	 * otras. Para ello, implementamos Consultas de Spring Data JPA Derivadas del
	 * Nombre del Metodo, cuya implementacion en SQL y mapeo Objeto-Relacional 
	 * es llevado a cabo por JPA de manera invisible al programador
	 * -----------------------------------------------------------------------------
	 */

	/**
	 * METODO
	 * ---------------------------------------------------------------------------------------
	 * Metodo que Busca en la Base de Datos a un USUARIO por su Email (que debe ser UNICO). 
	 * En SQL filtra con WHERE email=...
	 * 
	 * @param email
	 * @return
	 */
	Usuario findByEmail(String email);
}

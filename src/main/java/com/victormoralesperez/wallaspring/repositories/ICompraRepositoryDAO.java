package com.victormoralesperez.wallaspring.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.victormoralesperez.wallaspring.models.Compra;
import com.victormoralesperez.wallaspring.models.Usuario;

/**
 * INTERFAZ ICompraRepository
 * -------------------------------------------------------------------------------------------
 * Repositorio de Operaciones DAO (CRUD) asociadas a la Entidad COMPRA
 * 
 * @author Victor Morales Perez
 *
 */

public interface ICompraRepositoryDAO extends JpaRepository<Compra, Long> {

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
	 * Metodo que Busca en la Base de Datos todas las COMPRAS asociadas a un USUARIO
	 * 
	 * @param propietario
	 * @return
	 */
	List<Compra> findByPropietario(Usuario propietario);

}

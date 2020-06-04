package com.victormoralesperez.wallaspring.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.victormoralesperez.wallaspring.models.Compra;
import com.victormoralesperez.wallaspring.models.Producto;
import com.victormoralesperez.wallaspring.models.Usuario;

/**
 * INTERFAZ IProductoRepository
 * -------------------------------------------------------------------------------------------
 * Repositorio de Operaciones DAO (CRUD) asociadas a la Entidad PRODUCTO
 * 
 * @author Victor Morales Perez
 *
 */

public interface IProductoRepositoryDAO extends JpaRepository<Producto, Long> {

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
	 * Metodo que Busca en la Base de Datos todos los PRODUCTOS asociados a un USUARIO
	 * En SQL filtra por WHERE Propietario = ... 
	 * 
	 * @param propietario
	 * @return List de PRODUCTOS
	 */
	
	List<Producto> findByVendedor(Usuario usuarioVendedor);

	/**
	 * METODO
	 * ---------------------------------------------------------------------------------------
	 * Metodo que Busca en la Base de Datos todos los PRODUCTOS asociados a una COMPRA
	 * En SQL filtra por WHERE Compra = ... 
	 * 
	 * @param compra
	 * @return
	 */
	
	List<Producto> findByCompra(Compra compra);

	/**
	 * METODO
	 * ---------------------------------------------------------------------------------------
	 * Metodo que Busca en la Base de Datos todos los PRODUCTOS sin COMPRA asociada. 
	 * En SQL filtra por WHERE Compra = null (Que aun no han sido Vendidos)
	 * 
	 * @return List de PRODUCTOS
	 */
	
	List<Producto> findByCompraIsNull();

	/**
	 * METODO
	 * ---------------------------------------------------------------------------------------
	 * Metodo que Busca en la Base de Datos, filtrando por el nombre del producto, 
	 * los PRODUCTOS sin COMPRA asociada (Que aun no han sido Vendidos).
	 * En SQL filtra por WHERE nombre like... AND compra= null
	 * 
	 * @param nombre
	 * @return List de PRODUCTOS
	 */
	
	List<Producto> findByNombreContainsIgnoreCaseAndCompraIsNull(String nombre);

	/**
	 * METODO
	 * ---------------------------------------------------------------------------------------
	 * Metodo que Busca en la Base de Datos, filtrando por el nombre del producto 
	 * y el USUARIO que lo posee (lo haya registrado en la plataforma para venderlo
	 * o lo haya adquirido despues de hacer una Compra).
	 * En SQL filtra por WHERE nombre like... AND propietario = ...    
	 * 
	 * @param nombre
	 * @param propietario
	 * @return
	 */
	
	List<Producto> findByNombreContainsIgnoreCaseAndVendedor(String nombre, Usuario vendedor);

}

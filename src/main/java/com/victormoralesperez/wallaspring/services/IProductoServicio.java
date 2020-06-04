package com.victormoralesperez.wallaspring.services;

import java.util.List;

import com.victormoralesperez.wallaspring.models.Compra;
import com.victormoralesperez.wallaspring.models.Producto;
import com.victormoralesperez.wallaspring.models.Usuario;

/**
 * INTERFAZ IProductoServicio
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

public interface IProductoServicio {

	public Producto insertar(Producto p);
	public List<Producto> findAll();
	public Producto findById(long id);
	public List<Producto> buscarProductosPorId(List<Long> ids);
	public List<Producto> productosDeUnPropietario(Usuario u);
	public List<Producto> buscarMisProductos(String query, Usuario u);
	public List<Producto> productosDeUnaCompra(Compra c);
	public List<Producto> productosSinVender();
	public List<Producto> buscar(String query);
	public Producto editar(Producto p);
	public void borrar(long id);
	public void borrar(Producto p);
	
}

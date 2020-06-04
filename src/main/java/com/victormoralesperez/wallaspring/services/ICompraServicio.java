package com.victormoralesperez.wallaspring.services;

import java.util.List;

import com.victormoralesperez.wallaspring.models.Compra;
import com.victormoralesperez.wallaspring.models.Producto;
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

public interface ICompraServicio {
	
	public Compra crearCompra(Compra c);
	public Compra crearCompra(Compra c, Usuario u);
	public Producto addProductoCompra(Producto p, Compra c);
	public Compra buscarPorId(long id);
	public List<Compra> buscarTodas();
	public List<Compra> buscarTodasPorPropietario(Usuario u);

}

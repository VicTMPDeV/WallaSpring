package com.victormoralesperez.wallaspring.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.victormoralesperez.wallaspring.models.Compra;
import com.victormoralesperez.wallaspring.models.Producto;
import com.victormoralesperez.wallaspring.models.Usuario;
import com.victormoralesperez.wallaspring.repositories.ICompraRepositoryDAO;

/**
 * CLASE CompraServicioImpl
 * -------------------------------------------------------------------------------------------
 * Servicio que IMPLEMENTA (...Impl) el Interfaz ICompraServicio, que declara 
 * las operaciones asociadas a la Lógica de Negocio de la Entidad COMPRA. 
 * Hacemos uso implicitamente de los metodos de los Repositorios que acceden
 * a la Base de Datos, los cuales extraen la información de la Base de Datos
 * tal y como se encuentran dentro de esta, y en esta Capa nosotros nos
 * encargamos de tratar esa informacion, anyadirle funcionalidad asociada 
 * con la Logica de Negocio del programa, realizando procedimientos intermedios
 * con la informacion extraida de la Base de Datos o enviada a ella para ser
 * almacenados. Como, por ejemplo encriptar contrasenyas o eliminar ficheros 
 * asociados que no se almacenan en la Base de Datos. 
 * Esto lo hacemos asi para disminuir el acoplamiento del codigo, ya que el
 * Repositorio solo deberia saber que existe una Base de Datos con una
 * estructura pero NO como es la logica de negocio del programa en si. El
 * Servicio, se encarga de servir los datos del Repositorio tratados como 
 * la Logica de Negocio lo requiera, pero no sabe nada como es la Base de 
 * Datos (teniendo de este modo un Bajo Nivel de Acoplamiento en nuestro 
 * Codigo, lo cual nos permite sencillez en la Escalabilidad, el Mantenimiento
 * y la Reutilizacion del Codigo).
 * 
 * @author Victor Morales Perez
 *
 */

@Service
public class CompraServicioImpl implements ICompraServicio {

	/**
	 * ATRIBUTO
	 * ---------------------------------------------------------------------------------------
	 * Bean de Repositorio Auto-Inyectado (Spring con esta anotacion sabe que el Bean
	 * Repositorio se inyecta como Dependencia en la Clase CompraServicioImpl).
	 * Este Repositorio nos proporciona Metodos para acceder a la informacion de la 
	 * Tabla Compra de la Base de Datos.
	 */
	
	@Autowired
	ICompraRepositoryDAO repositorio;

	/**
	 * ATRIBUTO
	 * ---------------------------------------------------------------------------------------
	 * Bean de Servicio Auto-Inyectado (Spring con esta anotacion sabe que la Clase
	 * UsuarioServicio tiene como Dependencia un Servicio IProductoServicio).
	 */
	
	@Autowired
	IProductoServicio productoServicio; // UNA COMPRA ESTA COMPUESTA PRODUCTOS

	/**
	 * METODO CREAR NUEVA COMPRA
	 * -------------------------------------------------------------------------------------------
	 * Mapeamos el Metodo save(), que proporciona el Interfaz CrudRepository para
	 * CREAR una COMPRA nueva en el sistema. En este caso concreto no anyadimos nada
	 * nuevo a la funcionalidad que ya proporcionaba el Interfaz, pero se implementa
	 * por mantener la logica descrita al comienzo y darle escalabilidad (quien sabe
	 * si en el futuro la aplicacion crece y tenemos que implementar algun otro
	 * rasgo distintivo)
	 * 
	 * @param c
	 * @return
	 */

	@Override
	public Compra crearCompra(Compra c) {
		return repositorio.save(c);
	}

	/**
	 * METODO CREAR NUEVA COMPRA ASOCIADA A UN USUARIO
	 * -------------------------------------------------------------------------------------------
	 * Mapeamos el Metodo save(), que proporciona el Interfaz CrudRepository para
	 * CREAR una COMPRA, indicando el USUARIO asociado a dicha COMPRA. Ademas
	 * anyadimos un extra en la logica de negocio, la cual, tal y como la hemos
	 * planteado, le asigna un USUARIO Propietario a la COMPRA cuando esta se lleva
	 * a cabo.
	 * 
	 * @param c
	 * @param u
	 * @return
	 */
	
	@Override
	public Compra crearCompra(Compra c, Usuario u) {
		c.setPropietario(u);
		return repositorio.save(c);
	}

	/**
	 * METODO ANYADIR PRODUCTOS A LA COMPRA
	 * -------------------------------------------------------------------------------------------
	 * Mapeamos el Metodo que hemos anyadido en el Repositorio para Crear 1 PRODUCTO
	 * asociado a 1 COMPRA. Por como esta pensado el Modelo de Datos, la COMPRA NO
	 * CONOCE los PRODUCTOS que tiene asociados, sino que, como cada PRODUCTO solo
	 * puede ser Comprado 1 vez, es el propio PRODUCTO el que sabe a que COMPRA
	 * pertenece. Es por ello que editamos el PRODUCTO para darle un Valor concreto
	 * a su Atributo COMPRA.
	 * 
	 * @param p
	 * @param c
	 * @return
	 */
	
	@Override
	public Producto addProductoCompra(Producto p, Compra c) {
		p.setCompra(c);
		return productoServicio.editar(p);
	}

	/**
	 * METODO BUSCAR COMPRA POR ID
	 * -------------------------------------------------------------------------------------------
	 * Mapeamos el Metodo findById(), que proporciona el Interfaz JpaRepository
	 * Buscar 1 COMPRA por su ID (Clave Primaria). Ademas anyadimos un extra en la
	 * logica de negocio, dando una solucion si el Objeto COMPRA buscado no existe.
	 * 
	 * @param id
	 * @return
	 */
	
	@Override
	public Compra buscarPorId(long id) {
		return repositorio.findById(id).orElse(null);
	}

	/**
	 * METODO BUSCAR TODAS LAS COMPRAS EN EL SISTEMA
	 * -------------------------------------------------------------------------------------------
	 * Mapeamos el Metodo findAll(), que proporciona el Interfaz JpaRepository
	 * Buscar todos los PRODUCTOS existentes. En este caso concreto no anyadimos
	 * nada nuevo a la funcionalidad que ya proporcionaba el Interfaz, pero se
	 * implementa por mantener la logica descrita al comienzo y darle escalabilidad
	 * (quien sabe si en el futuro la aplicacion crece y tenemos que implementar
	 * algun otro rasgo distintivo).
	 * 
	 * @return
	 */
	
	@Override
	public List<Compra> buscarTodas() {
		return repositorio.findAll();
	}

	/**
	 * METODO BUSCAR TODAS LAS COMPRAS ASOCIADAS A UN PROPIETARIO
	 * -------------------------------------------------------------------------------------------
	 * Mapeamos el Metodo que hemos anyadido en el Repositorio para Buscar todas las
	 * COMPRAS asociadas a un USUARIO.
	 * 
	 * @param u
	 * @return
	 */
	
	@Override
	public List<Compra> buscarTodasPorPropietario(Usuario u) {
		return repositorio.findByPropietario(u);
	}

}

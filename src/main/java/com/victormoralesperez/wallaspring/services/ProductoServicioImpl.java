package com.victormoralesperez.wallaspring.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.victormoralesperez.wallaspring.models.Compra;
import com.victormoralesperez.wallaspring.models.Producto;
import com.victormoralesperez.wallaspring.models.Usuario;
import com.victormoralesperez.wallaspring.repositories.IProductoRepositoryDAO;
import com.victormoralesperez.wallaspring.storageservice.StorageService;

/**
 * CLASE ProductoServicioImpl
 * -------------------------------------------------------------------------------------------
 * Servicio que IMPLEMENTA (...Impl) el Interfaz IProductoServicio, que declara 
 * las operaciones asociadas a la Lógica de Negocio de la Entidad PRODUCTO. 
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
public class ProductoServicioImpl implements IProductoServicio{

	/**
	 * ATRIBUTO
	 * ---------------------------------------------------------------------------------------
	 * Bean de Repositorio Auto-Inyectado (Spring con esta anotacion sabe que el Bean
	 * Repositorio se inyecta como Dependencia en la Clase ProductoServicioImpl).
	 * Este Repositorio nos proporciona Metodos para acceder a la informacion de la 
	 * Tabla Producto de la Base de Datos.
	 */
	
	@Autowired
	IProductoRepositoryDAO repositorio;
	
	/**
	 * ATRIBUTO
	 * ---------------------------------------------------------------------------------------
	 * Servicio de Almacenamiento Auto-Inyectado (Spring con esta anotacion 
	 * sabe que el Bean storageService se inyecta como Dependencia en la 
	 * Clase ProductoServicio).
	 * 
	 * Este interfaz nos permite definir una abstracción de lo que debería
	 * ser un almacén secundario de información. 
	 * De esta forma, vamos a poder utilizar un almacen que acceda a nuestro 
	 * sistema de ficheros (también podríamos implementar otro que estuviera 
	 * en un sistema remoto).
	 */
	
	@Autowired
	StorageService storageService;
	
	/**
	 * METODO
	 * -------------------------------------------------------------------------------------------
	 * Mapeamos el Metodo save(), que proporciona el Interfaz JpaRepository 
	 * registrar PRODUCTO nuevo en el sistema.
	 * En este caso concreto no anyadimos nada nuevo a la funcionalidad que 
	 * ya proporcionaba el Interfaz, pero se implementa por mantener la logica
	 * descrita al comienzo y darle escalabilidad (quien sabe si en el futuro
	 * la aplicacion crece y tenemos que implementar algun otro rasgo distintivo).
	 * 
	 * @param p
	 * @return
	 */
	
	@Override
	public Producto insertar(Producto p) {
		return repositorio.save(p); 
	}
	
	/**
	 * METODO
	 * -------------------------------------------------------------------------------------------
	 * Mapeamos el Metodo findAll(), que proporciona el Interfaz JpaRepository
	 * Buscar todos los PRODUCTOS existentes.
	 * En este caso concreto no anyadimos nada nuevo a la funcionalidad que 
	 * ya proporcionaba el Interfaz, pero se implementa por mantener la logica
	 * descrita al comienzo y darle escalabilidad (quien sabe si en el futuro
	 * la aplicacion crece y tenemos que implementar algun otro rasgo distintivo).
	 * @return
	 */
	
	@Override
	public List<Producto> findAll(){
		return repositorio.findAll();
	}
	
	/**
	 * METODO
	 * -------------------------------------------------------------------------------------------
	 * Mapeamos el Metodo findById(), que proporciona el Interfaz JpaRepository 
	 * Buscar 1 PRODUCTO por su ID (Clave Primaria)
	 * Ademas anyadimos un extra en la logica de negocio, dando una solucion
	 * si el Objeto PRODUCTO buscado no existe.
	 * @param id
	 * @return
	 */
	
	@Override
	public Producto findById(long id) {
		return repositorio.findById(id).orElse(null);
	}
	
	/**
	 * READ
	 * -------------------------------------------------------------------------------------------
	 * Mapeamos el Metodo findAllById(), que proporciona el Interfaz JpaRepository 
	 * para Buscar todos los PRODUCTOS cuya ID (Clave Primaria) esté en la Lista
	 * que se pasa por Parametros.
	 * En este caso concreto no anyadimos nada nuevo a la funcionalidad que 
	 * ya proporcionaba el Interfaz, pero se implementa por mantener la logica
	 * descrita al comienzo y darle escalabilidad (quien sabe si en el futuro
	 * la aplicacion crece y tenemos que implementar algun otro rasgo distintivo).
	 * @param ids
	 * @return
	 */
	@Override
	public List<Producto> buscarProductosPorId(List<Long> ids){
		return repositorio.findAllById(ids);
	}
	
	/**
	 * METODO
	 * -------------------------------------------------------------------------------------------
	 * Mapeamos el Metodo que hemos anyadido en el Repositorio para
	 * Buscar todos los PRODUCTOS asociados a un USUARIO.
	 * @param u
	 * @return
	 */
	
	@Override
	public List<Producto> productosDeUnPropietario(Usuario u){
		return repositorio.findByPropietario(u);
	}
	
	/**
	 * METODO 
	 * -------------------------------------------------------------------------------------------
	 * Mapeamos el Metodo que hemos anyadido en el Repositorio para
	 * Buscar todos los PRODUCTOS asociados a un USUARIO, filtrando por el 
	 * Nombre del PRODUCTO y el USUARIO que lo posee (lo haya registrado en 
	 * la plataforma para venderlo o lo haya adquirido despues de Comprarlo)
	 * @param u
	 * @return
	 */
	
	@Override
	public List<Producto> buscarMisProductos(String query, Usuario u){
		return repositorio.findByNombreContainsIgnoreCaseAndPropietario(query, u);
	}
	
	/**
	 * METODO
	 * -------------------------------------------------------------------------------------------
	 * Mapeamos el Metodo que hemos anyadido en el Repositorio para
	 * Buscar todos los PRODUCTOS asociados a una COMPRA.
	 * En este caso concreto no anyadimos nada nuevo a la funcionalidad que 
	 * ya proporcionaba el Interfaz, pero se implementa por mantener la logica
	 * descrita al comienzo y darle escalabilidad (quien sabe si en el futuro
	 * la aplicacion crece y tenemos que implementar algun otro rasgo distintivo).
	 * @param c
	 * @return
	 */
	
	@Override
	public List<Producto> productosDeUnaCompra(Compra c){
		return repositorio.findByCompra(c);
	}
	
	/**
	 * METODO
	 * -------------------------------------------------------------------------------------------
	 * Mapeamos el Metodo que hemos anyadido en el Repositorio para
	 * Buscar todos los PRODUCTOS cuyo atributo COMPRA es NULL.
	 * En este caso concreto no anyadimos nada nuevo a la funcionalidad que 
	 * ya proporcionaba el Interfaz, pero se implementa por mantener la logica
	 * descrita al comienzo y darle escalabilidad (quien sabe si en el futuro
	 * la aplicacion crece y tenemos que implementar algun otro rasgo distintivo).
	 * @return
	 */
	
	@Override
	public List<Producto> productosSinVender(){
		return repositorio.findByCompraIsNull();
	}
	
	/**
	 * METODO
	 * -------------------------------------------------------------------------------------------
	 * Mapeamos el Metodo que hemos anyadido en el Repositorio para
	 * Buscar todos los PRODUCTOS, filtrando por el nombre del producto 
	 * y cuyo atributo COMPRA es NULL.
	 * En este caso concreto no anyadimos nada nuevo a la funcionalidad que 
	 * ya proporcionaba el Interfaz, pero se implementa por mantener la logica
	 * descrita al comienzo y darle escalabilidad (quien sabe si en el futuro
	 * la aplicacion crece y tenemos que implementar algun otro rasgo distintivo).
	 * @return
	 */
	
	@Override
	public List<Producto> buscar(String query){
		return repositorio.findByNombreContainsIgnoreCaseAndCompraIsNull(query);
	}
	
	/**
	 * METODO
	 * -------------------------------------------------------------------------------------------
	 * Mapeamos el Metodo save(), que proporciona el Interfaz JpaRepository
	 * para Editar un PRODUCTO pasado como Parametro.
	 * En este caso concreto estamos usando la funcionalidad del metodo
	 * guardar, para actuar sobre un PRODUCTO existente en la Base de Datos
 	 * "Machacando" la version existente por otra con los cambios realizados.
	 * @param p
	 * @return
	 */
	
	@Override
	public Producto editar(Producto p) {
		return repositorio.save(p);
	}
	
	/**
	 * METODO
	 * -------------------------------------------------------------------------------------------
	 * Mapeamos el Metodo deleteById(), que proporciona el Interfaz 
	 * JpaRepository para Borrar un PRODUCTO dado su ID.
	 * 
	 * @param id
	 */

	@Override
	public void borrar(long id) {
		repositorio.deleteById(id);
	}
	
	/**
	 * METODO
	 * -------------------------------------------------------------------------------------------
	 * Mapeamos el Metodo delete(), que proporciona el Interfaz 
	 * JpaRepository para Borrar un PRODUCTO pasado por Parametros.
	 * 
	 * @param id
	 */
	
	@Override
	public void borrar(Producto p) {
		repositorio.delete(p);
	}
}
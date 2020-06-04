package com.victormoralesperez.wallaspring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.victormoralesperez.wallaspring.models.Usuario;
import com.victormoralesperez.wallaspring.repositories.IUsuarioRepositoryDAO;

/**
 * CLASE UsuarioServicioImpl
 * -------------------------------------------------------------------------------------------
 * Servicio que IMPLEMENTA (...Impl) el Interfaz IUsuarioServicio, que declara 
 * las operaciones asociadas a la Lógica de Negocio de la Entidad USUARIO. 
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
public class UsuarioServicioImpl implements IUsuarioServicio {

	/**
	 * ATRIBUTO
	 * ---------------------------------------------------------------------------------------
	 * Bean de Repositorio Auto-Inyectado (Spring con esta anotacion sabe que el Bean
	 * Repositorio se inyecta como Dependencia en la Clase UsuarioServicioImpl).
	 * Este Repositorio nos proporciona Metodos para acceder a la informacion de la 
	 * Tabla Usuario de la Base de Datos.
	 */
	
	@Autowired
	IUsuarioRepositoryDAO repositorio;

	/**
	 * ATRIBUTO
	 * ---------------------------------------------------------------------------------------
	 * Encriptador Auto-Inyectado (Spring con esta anotacion sabe que el Bean
	 * passwordEncoder se inyecta como Dependencia en la Clase UsuarioServicio).
	 * 
	 * El Password lo recibiremos desde el Formulario, pero antes de pasarselo
	 * al Constructor de la Clase Usuario, lo Encriptaremos.
	 */
	@Autowired
	BCryptPasswordEncoder passwordEncoder; 										

	/**
	 * METODO
	 * -------------------------------------------------------------------------------------------
	 * Mapeamos el Metodo save(), que proporciona el Repositorio para 
	 * registrar USUARIO nuevo en el sistema, ENCRIPTANDO su PassWord.
	 * 
	 * @param user
	 * @return
	 */
	
	@Override
	public Usuario registrar(Usuario user) {
		user.setPassword(passwordEncoder.encode(user.getPassword())); 	// Primero cogemos el Password y lo ENCRIPTAMOS.
		return repositorio.save(user);									// Una vez Encriptado, guardamos el USUARIO.
	}

	/**
	 * METODO
	 * -------------------------------------------------------------------------------------------
	 * Mapeamos el Metodo findById(), que proporciona el Repositorio para 
	 * Buscar USUARIO por su ID (Clave Primaria), y que ademas nos permita
	 * hacer algo en la logica de negocio si no existe.
	 * 
	 * @param id
	 * @return Devuelve un Objeto Usuario o NULL si no existe.
	 */
	
	@Override
	public Usuario findById(long id) {
		return repositorio.findById(id).orElse(null);
	}

	/**
	 * METODO
	 * -------------------------------------------------------------------------------------------
	 * Mapeamos el Metodo findByEmail(), que proporciona el Repositorio para 
	 * Buscar USUARIO por su Email (Clave Unica).
	 * En este caso concreto no anyadimos nada nuevo a la funcionalidad que 
	 * ya proporcionaba el Interfaz, pero se implementa por mantener la logica
	 * descrita al comienzo y darle escalabilidad (quien sabe si en el futuro
	 * la aplicacion crece y tenemos que implementar algun otro rasgo distintivo).
	 * 
	 * @param email
	 * @return
	 */
	
	@Override
	public Usuario buscarPorEMail(String email) {
		return repositorio.findByEmail(email);
	}
	
	/**
	 * METODO
	 * -------------------------------------------------------------------------------------------
	 * Mapeamos el Metodo save(), que proporciona el Interfaz JpaRepository
	 * para Editar un USUARIO pasado como Parametro.
	 * En este caso concreto estamos usando la funcionalidad del metodo
	 * guardar, para actuar sobre un USUARIO existente en la Base de Datos
 	 * "Machacando" la version existente por otra con los cambios realizados.
 	 * (CUIDADO, SOLO EDITAREMOS EN CAPAS MAS ALTAS DE NUESTRA ARQUITECTURA
 	 * LOS CAMPOS JUSTOS Y NECESARIOS PARA NO ROMPER LA INTEGRIDAD REFERENCIAL
 	 * ENTRE USUARIOS - COMPRAS - PRODUCTOS).
 	 * 
	 * @param user
	 * @return
	 */

	@Override
	public Usuario editar(Usuario user) {
		return repositorio.save(user);
	}

}

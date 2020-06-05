package com.victormoralesperez.wallaspring;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.victormoralesperez.wallaspring.models.Producto;
import com.victormoralesperez.wallaspring.models.Usuario;
import com.victormoralesperez.wallaspring.services.IProductoServicio;
import com.victormoralesperez.wallaspring.services.IUsuarioServicio;
import com.victormoralesperez.wallaspring.services.ProductoServicioImpl;
import com.victormoralesperez.wallaspring.services.UsuarioServicioImpl;
import com.victormoralesperez.wallaspring.storageservice.StorageProperties;
import com.victormoralesperez.wallaspring.storageservice.StorageService;

/**
 * CLASE WallaSpringApplication
 * -------------------------------------------------------------------------------------------
 * Contiene todo lo necesario para poder lanzar la Aplicacion.
 * 
 * @SpringBootApplication : Esta anotación se encarga de CONFIGURAR 
 * nuestra aplicación en función de las DEPENDENCIAS que encuentre 
 * en el CLASSPATH del proyecto. 
 * Le dice a Spring Boot que comience a agregar BEANS según sea 
 * la configuración del Classpath, de las Properties, etc...
 * Por ejemplo, si Spring-Web MVC está en el Classpath 
 * (en forma de dependencia starter), esta anotación le dice a 
 * la Aplicación que se va a comportar como una aplicación web 
 * y activa comportamientos clave, como configurar un 
 * DispatcherServlet o saber donde buscar las 
 * propiedades de Almacenamiento, etc.
 * 
 * @EnableConfigurationProperties : Esta anotación le dice a
 * SpringBoot que inyecte como Beans de Configuracion la 
 * configuracion del Servicio de Almacenamiento
 * (Basicamente la ruta donde vamos a almacenar los ficheros)
 * 
 * @author Victor Morales Perez
 * 
 */

@EnableConfigurationProperties(StorageProperties.class)
@SpringBootApplication
public class WallaSpringApplication {
	
	/**
	 * ATRIBUTO
	 * ---------------------------------------------------------------------------------------
     * Este Bean se inicia al lanzar la aplicación. Nos permite 
     * inicializar el almacenamiento secundario del proyecto
     *
     * @param storageService 
     * @return
     */
	
    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
        	// storageService.deleteAll(); //BORRA TODO EL ALMACENAMIENTO EN LA CARPETA upload-dir (imagenes) ANTES DE EJECUTAR - COMENTAR CUANDO PASEMOS A PERSISTENCIA DE DATOS EN MySQL
            // Iniciamos el Servicio de Almacenamiento en el Directorio indicado en StorageProperties.
            storageService.init();
        };
    }
	
    /**
	 * METODO MAIN - PUNTO DE ENTRADA A LA APLICACION WEB
	 * ---------------------------------------------------------------------------------------
	 * Arranca la Aplicacion cargando la configuracion necesaria proporcionada por la propia
	 * Clase y los Argumentos que se le pasan como parametros de inicio (cargar datos de 
	 * prueba proporcionados por el Metodo CommandLineRunner initData() en este nuestro caso)
	 * 
	 */
	public static void main(String[] args) {
		
		SpringApplication.run(WallaSpringApplication.class, args);
		
	}
	
	
	
	/**
	 * METODO DE CARGA DE DATOS DE INICIO EN LA APLICACION
	 * ---------------------------------------------------------------------------------------
	 * Carga algunos Datos de Inicio haciendo uso de los Servicios Implementados que comunican 
	 * la Capa de Persistencia con la Lógica de Negocio. 
	 * Al inicio no existe ninguna entidad Compra porque estas entidades se inicializan cuando 
	 * ocurre la accion de FINALIZAR LA COMPRA, ya dentro de la Aplicación. (ver CompraController).
	 * 
	 * IMPORTANTE: Este metodo y su ejecucion, una vez queramos desplegar la Aplicacion, 
	 * hay que DEJARLO COMENTADO porque los registros que genera en la Base de Datos 
	 * de MySQL ya quedan persistidos.
	 * 
	 * @param usuarioServicio
	 * @param productoServicio
	 * @return
	 */
	
//	@Bean
//	public CommandLineRunner initData(IUsuarioServicio usuarioServicio, IProductoServicio productoServicio) {
//		return args -> {
//			//CREAMOS DOS USUARIOS DE PRUEBA AL DESPLEGAR LA APLICACION
//			Usuario user1 = new Usuario("Víctor", "Morales Pérez", null, "victor.aparejador.1986@gmail.com", "mokete");
//			user1 = usuarioServicio.registrar(user1);
//			Usuario user2 = new Usuario("Maria Dolores", "Fernández Castillo", null, "lolifer85@gmail.com", "monete");
//			user2 = usuarioServicio.registrar(user2);
//			//CREAMOS UNA LISTA DE PRODUCTOS DE PRUEBA DISPONIBLES AL DESPLEGAR LA APLICACION
//			List<Producto> productList = Arrays.asList
//					(new Producto("Bicicleta de Montaña", 100.0f,null, user1),
//					 new Producto("Golf GTI Serie 2", 2500.0f,null,user1),
//					 new Producto("Xbox One X", 425.0f,null, user2),
//					 new Producto("Cerveza Fresquita", 1.50f,null,user2),
//					 new Producto("Pipi de Gato", 1.00f,null,user2));
//			//HACIENDO USO DEL SERVICIO, LOS AGREGAMOS A LA BASE DE DATOS USANDO JPA
//			for (Producto p : productList) {
//				productoServicio.insertar(p);
//			}
//		};
//	}
	
}

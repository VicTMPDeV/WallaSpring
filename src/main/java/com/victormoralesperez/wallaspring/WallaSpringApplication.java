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
        	storageService.deleteAll();
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
	 * @param usuarioServicio
	 * @param productoServicio
	 * @return
	 */
	
	
	//ESTO LO PODEMOS ELIMINAR ANTES DE LA PRODUCCION SI CONSEGUIMOS TENER LA BASE DE DATOS 
	//EXTERNA AL PROGRAMA , EN MYSQL POR EJEMPLO, CON ESTOS DATOS YA PERSISTENTES EN ELLA.
	
	// LO QUE SI QUE HABRIA QUE HACER ES UNA LECTURA DE LA BASE DE DATOS AL INICIAR LA APLICACION
	// PREGUNTAR AL ORACULO COMO SE HACE
	
	
	@Bean
	public CommandLineRunner initData(IUsuarioServicio usuarioServicio, IProductoServicio productoServicio) {
		return args -> {
			//CREAMOS DOS USUARIOS DE PRUEBA AL DESPLEGAR LA APLICACION
			Usuario user1 = new Usuario("Víctor", "Morales Pérez", null, "victor.aparejador.1986@gmail.com", "mokete");
			user1 = usuarioServicio.registrar(user1);
			Usuario user2 = new Usuario("Maria Dolores", "Fernández Castillo", null, "lolifer85@gmail.com", "monete");
			user2 = usuarioServicio.registrar(user2);
			//CREAMOS UNA LISTA DE PRODUCTOS DE PRUEBA DISPONIBLES AL DESPLEGAR LA APLICACION
			List<Producto> productList = Arrays.asList
					(new Producto("Bicicleta de montaña", 100.0f,"https://sgfm.elcorteingles.es/SGFM/dctm/MEDIA03/201809/20/00108451997269____4__640x640.jpg", user1),
					 new Producto("Golf GTI Serie 2", 2500.0f,"https://www.minicar.es/large/Volkswagen-Golf-GTi-G60-Serie-II-%281990%29-Norev-1%3A18-i22889.jpg",user1),
					 new Producto("Xbox One X", 425.0f, "upload-dir/Xbox.jpg", user2),
					 new Producto("Trípode flexible", 10.0f, "https://images.vibbo.com/635x476/860/86074256163.jpg",user2),
					 new Producto("Iphone 7 128 GB", 350.0f,"https://store.storeimages.cdn-apple.com/4667/as-images.apple.com/is/image/AppleInc/aos/published/images/i/ph/iphone7/rosegold/iphone7-rosegold-select-2016?wid=470&hei=556&fmt=jpeg&qlt=95&op_usm=0.5,0.5&.v=1472430205982",user2));
			//HACIENDO USO DEL SERVICIO, LOS AGREGAMOS A LA BASE DE DATOS USANDO JPA
			for (Producto p : productList) {
				productoServicio.insertar(p);
			}
		};
	}
	
}

package com.victormoralesperez.wallaspring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.victormoralesperez.wallaspring.storageservice.StorageService;

/**
 * CLASE FilesController
 * -------------------------------------------------------------------------------------------
 * Clase CONTROLADOR que atiende las Peticiones (Request) y despacha las
 * Respuestas (Response) de todas las funcionalidades relacionadas con el
 * Acceso a los Ficheros Almacenados por el Sistema de Almacenamiento de la
 * Aplicacion.
 * 
 * @author Victor Morales Perez
 *
 */

@Controller
public class FilesController {
	
	/**
	 * ATRIBUTO
	 * ---------------------------------------------------------------------------------------
	 * Bean de Servicio Auto-Inyectado (Spring con esta anotacion sabe que el Bean
	 * storageService se inyecta como Dependencia en la Clase ProductoController).
	 * Este Servicio nos proporciona los mecanismos necesarios para poder almacenar
	 * las imagenes tanto de los Avatares de los Usuarios como de los Productos en
	 * un fichero de almacenamiento interno del sistema.
	 */
	
	@Autowired
	StorageService storageService;
	
	/**
	 * METODO QUE NOS DEVUELVE UN FICHERO
	 * ---------------------------------------------------------------------------------------
	 * Metodo que atiende una Peticion Get en la ruta de almacenamiento que 
	 * hemos definido para los ficheros de almacenamiento interno del sistema
	 * Implementar este Metodo tiene como ventaja NO tener que configurar el
	 * almacenamiento estatico para obtener ficheros.
	 * La Anotacion @ResponseBody indica que el Metodo NO nos Reenvia a una 
	 * Vista, sino que DEVUELVE UN RECURSO (un fichero en este caso).
	 * Para definir la URL, empleamos una Expresion GLOB que representa lo
	 * que esta en ese punto de la ruta MAS un contenido que se encuentra
	 * en esa misma Ruta, con ello le asigna su valor a una Variable 
	 * {filename:.+VARIABLE}, que gracias a la Expresion GLOB nos da una URL
	 * relativa completa apuntando hacia el Recurso buscado.
	 * Empleamos la Interfaz Resource, que es un Envoltorio de un Recurso
	 * (fichero binario, fichero de classpath, etc... nos sirve para
	 * representar cualquier tipo de fichero). 
	 * Empleando el Servicio de Almacenamiento (Interfaz StorageService),
	 * Cargamos el archivo (en esta aplicacion web seran archivos de 
	 * imagenes para Productos o Avatares de Usuario) como un Resource,
	 * que nos aisla del lugar en el que este almacenado (dentro de algun
	 * fichero de la aplicacion, proveniente de una API RESTful...)
	 * Finalmente, DEVUELVE un Objeto ResponseEntity (Entidad de Respuesta),
	 * que nos permite "montar" la Respuesta HTTP, definiendo con el metodo
	 * ok() que la Respuesta sera un Codigo 200 (todo ok) y el fichero
	 * de la Imagen que queremos obtener como Cuerpo de la Respuesta.
	 * 
	 * @param filename
	 * @return Fichero de Imagen
	 */

	
    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().body(file);
    }

}

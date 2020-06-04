package com.victormoralesperez.wallaspring.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.victormoralesperez.wallaspring.models.Producto;
import com.victormoralesperez.wallaspring.services.IProductoServicio;

/**
 * CLASE PublicController
 * -------------------------------------------------------------------------------------------
 * Clase CONTROLADOR que atiende las Peticiones (Request) y despacha las
 * Respuestas (Response) en toda la Zona PUBLICA ("/public") de la aplicacion 
 * (lo que se ve SIN necesidad de AUTENTICARSE)
 * 
 * @author Victor Morales Perez
 *
 */

@Controller
@RequestMapping("/public") // URL de base para todo el Controlador, de Dominio Publico
public class PublicController {

	/**
	 * ATRIBUTO
	 * ---------------------------------------------------------------------------------------
	 * Bean de Servicio Auto-Inyectado (Spring con esta anotacion sabe que el Bean
	 * productoServicio se inyecta como Dependencia en la Clase PublicController).
	 * Este Servicio nos proporciona un metodo de obtencion de datos relacionados
	 * con la Entidad Producto de la Base de Datos, haciendo uso de los Repositorios 
	 * que mapean dicha informacion relacional en Objetos.
	 */

	@Autowired
	IProductoServicio productoServicio;

	/**
	 * METODO
	 * ---------------------------------------------------------------------------------------
	 * Los Metodos con la Anotacion @ModelAttribute inyectan en el Model el VALOR
	 * que devuelven, indicando una CLAVE (Model es un MAP). Dicha CLAVE
	 * ("productos") sera mapeada en la VISTA y en ella se podra extraer el VALOR
	 * devuelto por la funcion e inyectarlo en la VISTA como un Atributo del Model. 
	 * De esta manera siempre tenemos los productos NO Vendidos visibles desde la 
	 * Zona Publica (Lo que se ve Sin Loguearse)
	 *
	 * @return
	 */
	
	@ModelAttribute("productos")
	public List<Producto> productosNoVendidos() {
		return productoServicio.productosSinVender();
	}

	/**
	 * METODO
	 * ---------------------------------------------------------------------------------------
	 * Metodo que atiende una Peticion GET en las 2 rutas que se indican. Nos muestra
	 * los productos disponibles sin vender en el Index de la pagina. Recibe por
	 * Parametros una Query que no es obligatoria y que si tiene algun valor sera
	 * porque estamos buscando algun producto concreto, y un objeto de la Clase
	 * Model (Model es un Map que nos permite pasar Objetos del Controlador a la
	 * Vista). Si la Query trae consigo algun valor, se lo inyectamos al Model con
	 * la Clave "productos" y Valor el devuelto por la busqueda realizada por el
	 * Servicio hacia la BD usando el Repositorio. Si lo encuentra, lo muestra. De
	 * no haber ningun producto que se corresponda con el buscado o no existir una
	 * query (no hemos querido buscar nada), la Clave "productos" del Model seguira
	 * mostrando todos los productosSinVender(). El metodo devuelve un String que es
	 * la ruta de la plantilla html, pero sin indicar la extension de la misma.
	 * 
	 * @param model
	 * @param query
	 * @return
	 */

	@GetMapping({ "/", "/index" })
	public String index(@RequestParam(name = "q", required = false) String query, Model model) {
		if (query != null) {
			model.addAttribute("productos", productoServicio.buscar(query));
		}
		return "index";
	}

	/**
	 * METODO
	 * ---------------------------------------------------------------------------------------
	 * Metodo que atiende una Peticion GET en la ruta "/producto" y con una Variable 
	 * {id} en la URL que sera inyectada pasandola por parametros con la
	 * anotacion @PathVariable y el tipo de dato de la misma y que se usara para
	 * realizar una busqueda realizada por el Servicio hacia la BD usando el
	 * Repositorio de Productos. En caso de encontrar el producto con el id
	 * asociado, lo anyade al Model para enviarlo a la Vista con la CLAVE "producto"
	 * y VALOR el obtenido por el metodo del Repositorio. De no encontrar resultado,
	 * sigue mostrando todos los productos dado que no hemos anyadido nada nuevo al
	 * Model.
	 * @param id
	 * @param model
	 * @return
	 */

	@GetMapping("/producto/{id}")
	public String showProduct(@PathVariable Long id, Model model) {
		Producto result = productoServicio.findById(id);
		if (result != null) {
			model.addAttribute("producto", result);
			return "producto";
		}
		return "redirect:/public";
	}

	/**
	 * METODO
	 * ---------------------------------------------------------------------------------------
	 * Metodo que atiende una Peticion GET en la ruta "/about_me" y que envia a una Pagina
	 * de Contenido Estatico.
	 * Es el Controlador mas Simple que nos podemos encontrar.
	 * No requiere ni hacer uso de la Clase Model porque no pasamos informacion, solamente
	 * mostrara el contenido estatico que tenga la pagina.
	 * 
	 * Son los Creditos de la Pelicula y el momento en que dejo de picar codigo para el 
	 * Proyecto de Fin de Ciclo...
	 * 
	 * Jose Luis Gonzalez Sanchez (aka Oraculo), Muchas Gracias por todas tus ensenyanzas,
	 * has sido y eres la luz que ha iluminado mi senda para descubrir el mundo de los 
	 * Frameworks, los Patrones de Diseño Software, Arquitecturas, Buenas Practicas a la 
	 * hora de Programar, etc... No puedo mas que darte las gracias siempre, sin tus
	 * consejos jamas podria haber aprendido tanto antes de chocarme contra el mundo laboral.
	 * 
	 *  P.D.: Ala, que te la pique un pollo, tira!!
	 * 
	 * @return
	 */
	
	@GetMapping("/about_me")
    public String info() {
    	return "about_me";
    }
	
}

package com.victormoralesperez.wallaspring.controllers;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.victormoralesperez.wallaspring.models.Compra;
import com.victormoralesperez.wallaspring.models.Producto;
import com.victormoralesperez.wallaspring.models.Usuario;
import com.victormoralesperez.wallaspring.reports.GeneradorPDF;
import com.victormoralesperez.wallaspring.reports.Html2PdfService;
import com.victormoralesperez.wallaspring.services.ICompraServicio;
import com.victormoralesperez.wallaspring.services.IProductoServicio;
import com.victormoralesperez.wallaspring.services.IUsuarioServicio;

/**
 * CLASE CompraController
 * -------------------------------------------------------------------------------------------
 * Clase CONTROLADOR que atiende las Peticiones (Request) y despacha las
 * Respuestas (Response) en toda la Zona PRIVADA ("/app") de la aplicacion 
 * (lo que se ve una vez REGISTRADO y AUTENTICADO)
 * 
 * @author Victor Morales Perez
 *
 */

@Controller
@RequestMapping("/app") // URL de base para todo el Controlador, de Dominio Privado
public class CompraController {

	/**
	 * ATRIBUTO
	 * ---------------------------------------------------------------------------------------
	 * Bean de Servicio Auto-Inyectado (Spring con esta anotacion sabe que el Bean
	 * compraServicio se inyecta como Dependencia en la Clase CompraController).
	 * Este Servicio nos proporciona un metodo de obtencion de datos relacionados
	 * con la Entidad Compra de la Base de Datos, haciendo uso de los Repositorios
	 * que mapean dicha informacion relacional en Objetos.
	 */
	
	@Autowired
	ICompraServicio compraServicio;

	/**
	 * ATRIBUTO
	 * ---------------------------------------------------------------------------------------
	 * Bean de Servicio Auto-Inyectado (Spring con esta anotacion sabe que el Bean
	 * productoServicio se inyecta como Dependencia en la Clase ProductoController).
	 * Este Servicio nos proporciona un metodo de obtencion de datos relacionados
	 * con la Entidad Producto de la Base de Datos, haciendo uso de los Repositorios
	 * que mapean dicha informacion relacional en Objetos.
	 */
	
	@Autowired
	IProductoServicio productoServicio;

	/**
	 * ATRIBUTO
	 * ---------------------------------------------------------------------------------------
	 * Bean de Servicio Auto-Inyectado (Spring con esta anotacion sabe que el Bean
	 * usuarioServicio se inyecta como Dependencia en la Clase ProductoController).
	 * Este Servicio nos proporciona un metodo de obtencion de datos relacionados
	 * con la Entidad Usuario de la Base de Datos, haciendo uso de los Repositorios
	 * que mapean dicha informacion relacional en Objetos.
	 */
	
	@Autowired
	IUsuarioServicio usuarioServicio;

	/**
	 * ATRIBUTO
	 * ---------------------------------------------------------------------------------------
	 * Bean de Sesion Auto-Inyectado (Spring con esta anotacion sabe que el Bean
	 * session (Sesion del Usuario a la que va asociado el Carrito)
	 * se inyecta como Dependencia en la Clase CompraController)
	 */
	
	@Autowired
	HttpSession session; 
	
	/**
	 * ATRIBUTO
	 * ---------------------------------------------------------------------------------------
	 * Bean de Servicio Auto-Inyectado (Spring con esta anotacion sabe que el Bean
	 * documentGeneratorService (Servicio que genera un informe en PDF de la Compra)
	 * se inyecta como Dependencia en la Clase CompraController)
	 */
	
    @Autowired
    Html2PdfService documentGeneratorService;

    /**
	 * ATRIBUTO
	 * ---------------------------------------------------------------------------------------
	 * Bean POJO Auto-Inyectado (Spring con esta anotacion sabe que el Bean
	 * usuarioPropietario se inyecta como Dependencia en la Clase
	 * ProductoController). Este Objeto Usuario es una Dependencia de la Clase
	 * Producto. representa al Usuario (AUTENTICADO en el momento de realizar 
	 * las operaciones) que tiene en Propiedad el Producto.
	 */
	
	private Usuario comprador; 

	/**
	 * METODO
	 * ---------------------------------------------------------------------------------------
	 * Los Metodos con la Anotacion @ModelAttribute escriben en el Model el VALOR
	 * que devuelven, indicando una CLAVE (Model es un MAP). Dicha CLAVE
	 * ("carrito") sera leida desde la VISTA y en ella se podra extraer el VALOR
	 * devuelto por la funcion e inyectarlo en la VISTA como un Atributo del
	 * Model.
	 * El contenido del Carrito lo extraemos de la Sesion y es un Listado de ID de Productos.	
	 *
	 * @return
	 */

	@ModelAttribute("carrito") 
	public List<Producto> productosCarrito() {
		// Obtengo una lista de id alacenados en la sesión como "carrito" del Usuario
		List<Long> contenido = (List<Long>) session.getAttribute("carrito"); 
		// Devulevo la lista de productos que tienen la id almacenada en la sesion
		return (contenido == null) ? null : productoServicio.buscarProductosPorId(contenido);
	}
	
	/**
	 * METODO
	 * ---------------------------------------------------------------------------------------
	 * Los Metodos con la Anotacion @ModelAttribute escriben en el Model el VALOR
	 * que devuelven, indicando una CLAVE (Model es un MAP). Dicha CLAVE
	 * ("items_carrito") sera mapeada en la VISTA y en ella se podra extraer el
	 * VALOR devuelto por la funcion e inyectarlo en la VISTA como un Atributo del
	 * Model.
	 * El numero de items (productos) del Carrito lo extraemos invocando al metodo 
	 * productosCarrito() y, si no esta vacio, obtenemos el tamaño del List (devuelve
	 * el numero de elementos dentro del listado) y lo parseamos el tipo de dato a String
	 * para despues, poderlo escribir en el navbar de la vista.
	 *
	 * @return Numero de items en el Carrito
	 */
	
    @ModelAttribute("items_carrito")
    public String itemsCarrito() {
        List<Producto> productosCarrito = productosCarrito(); 
        if (productosCarrito != null)
            return Integer.toString(productosCarrito.size());
        return "";
    }

	/**
	 * METODO
	 * ---------------------------------------------------------------------------------------
	 * Los Metodos con la Anotacion @ModelAttribute escriben en el Model el VALOR
	 * que devuelven, indicando una CLAVE (Model es un MAP). Dicha CLAVE
	 * ("total_carrito") sera mapeada en la VISTA y en ella se podra extraer el
	 * VALOR devuelto por la funcion e inyectarlo en la VISTA como un Atributo del
	 * Model.
	 * El contenido del Carrito lo extraemos invocando al metodo productosCarrito() y
	 * Si el Carrito no esta vacio, sumamos el precio de todos los Productos, extrayendo
	 * el precio de cada Producto en el List y acumulandolo sucesivamente dentro de un 
	 * Bucle ForEach.
	 * Finalmente, si no hay productos en el carrito, devolvemos 0.0, que es el valor al
	 * que hemos inicializado la variable totalCarrito antes de entrar en el bucle.	
	 *
	 * @return Coste Total de los Productos existentes en el Carrito o Cero si esta vacio
	 */

	@ModelAttribute("total_carrito")
	public Float totalCarrito() {
		List<Producto> productosCarrito = productosCarrito(); 
		Float totalCarrito = 0.0F;
		if (productosCarrito != null) { 
			for (Producto producto : productosCarrito) {
				totalCarrito += producto.getPrecio();
			}
		}
		return totalCarrito;
	}

	/**
	 * METODO
	 * ---------------------------------------------------------------------------------------
	 * Los Metodos con la Anotacion @ModelAttribute escriben en el Model el VALOR
	 * que devuelven, indicando una CLAVE (Model es un MAP). Dicha CLAVE
	 * ("mis_compras") sera mapeada en la VISTA y en ella se podra extraer el
	 * VALOR devuelto por la funcion e inyectarlo en la VISTA como un Atributo del
	 * Model.
	 * Extraemos el Username (email) del Usuario que esta Autenticado.
	 * Buscamos en la Base de Datos al Propietario y consultamos todas
	 * las compras asociadas a ese Email que se ha registrado en la Sesion.
	 * 
	 * @return 
	 */
	
	@ModelAttribute("mis_compras")
	public List<Compra> misCompras() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName(); 
		comprador = usuarioServicio.buscarPorEMail(email);
		return compraServicio.buscarTodasPorPropietario(comprador);
	}

	/**
	 * METODO
	 * ---------------------------------------------------------------------------------------
	 * Metodo que mapea una Peticion GET en la ruta /carrito para mostrar la 
	 * plantilla que muestra el contenido del Carrito.
	 * Como ya tenemos los Metodos (productosCarrito() y totalCarrito()) anotados
	 * con @ModelAttribute, que se encargan de obtener la informacion del Carrito
	 * e inyectarla en el Model de "carrito" y "total_carrito" respectivamente...
	 * tan solo nos queda dirigir a la plantilla carrito.html.
	 * 
	 * @param model
	 * @return
	 */
    
	@GetMapping("/carrito")
	public String verCarrito(Model model) {
		return "app/compra/carrito";
	}
	
	/**
	 * METODO PARA ANYADIR PRODUCTOS AL CARRITO
	 * ---------------------------------------------------------------------------------------
	 * Metodo que atiende una Peticion GET en la ruta "/carrito/add/{id}" para
	 * Insertar un Producto en el Carrito. Recibe una Variable en la URL que es 
	 * el Identificador del Producto que se quiere anyadir al Carrito, se pasa
	 * por Parametros con la anotacion @PathVariable y el tipo de dato de la misma 
	 * y que se usara para realizar una busqueda realizada por el Servicio hacia 
	 * la BD usando el Repositorio de Productos.
	 * Primero RECUPERAMOS de la SESION del Usuario el contenido del Carrito 
	 * (Puede haber estado comprando previamente y esos articulos en el 
	 * Carrito deben persistirse el tiempo que dure la Sesion).
	 * Si el Carrito esta vacio, generamos una Lista para anyadir Productos en 
	 * ella. Si NO esta vacio, comprobamos que el Producto que queremos anyadir
	 * no esta previamente anyadido (esto es asi porque solo podemos Comprar UN
	 * Producto con UN id). Si no esta previamente, lo anyadimos al Carrito.
	 * Inyectamos en la Sesion del Usuario Autenticado el Producto agregado al 
	 * Carrito e indicamos el Tamanyo de la Lista de Productos que hay en el.
	 * Finalmente, redireccionamos a la Pagina del Carrito.
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	
	@GetMapping("/carrito/add/{id}")
	public String addCarrito(Model model, @PathVariable Long id) {
		List<Long> contenidoCarrito = (List<Long>) session.getAttribute("carrito"); 
		if (contenidoCarrito == null) {
			contenidoCarrito = new ArrayList<>(); 
		}
		if (!contenidoCarrito.contains(id)) { 
			contenidoCarrito.add(id); 
		}
		session.setAttribute("carrito", contenidoCarrito); 
		session.setAttribute("items_carrito", contenidoCarrito.size());
		return "redirect:/app/carrito"; 
	}

	/**
	 * METODO PARA ELIMINAR PRODUCTOS DEL CARRITO DE LA COMPRA
	 * ---------------------------------------------------------------------------------------
	 * Metodo que atiende una Peticion GET en la ruta "/carrito/eliminar/{id}" para
	 * Eliminar un Producto del Carrito. Recibe una Variable en la URL que es 
	 * el Identificador del Producto que se quiere anyadir al Carrito, se pasa
	 * por Parametros con la anotacion @PathVariable y el tipo de dato de la misma 
	 * y que se usara para realizar una busqueda realizada por el Servicio hacia 
	 * la BD usando el Repositorio de Productos.
	 * Primero RECUPERAMOS de la SESION del Usuario el contenido del Carrito 
	 * (Puede haber estado comprando previamente y esos articulos en el 
	 * Carrito deben persistirse el tiempo que dure la Sesion).
	 * Si el Carrito esta vacio, redireccionamos a la Pagina Principal.
	 * Si NO esta vacio, Borramos de la Lista el Producto con el ID indicado.
	 * Si despues de Borrar el Producto, el Carrito queda Vacio, eliminamos
	 * el Carrito de la Sesion.
	 * Si despues de Borrar el Producto, quedan Productos en el Carrito,
	 * ACTUALIZAMOS el Contenido del Carrito y el Numero de Productos que
	 * contiene, inyectandolo en la Sesion.
	 * Finalmente, redireccionamos a la Pagina del Carrito.
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	
	@GetMapping("/carrito/eliminar/{id}")
	public String borrarDeCarrito(Model model, @PathVariable Long id) {
		List<Long> contenidoCarrito = (List<Long>) session.getAttribute("carrito");
		if (contenidoCarrito == null) {
			return "redirect:/public";										
		}
		contenidoCarrito.remove(id);
		if (contenidoCarrito.isEmpty()) {
			session.removeAttribute("carrito");
			session.removeAttribute("items_carrito");
		} else {
			session.setAttribute("carrito", contenidoCarrito);
			session.setAttribute("items_carrito", contenidoCarrito.size());
		}
		return "redirect:/app/carrito";
	}

	/**
	 * METODO PARA FINALIZAR LA COMPRA
	 * ---------------------------------------------------------------------------------------
	 * Metodo que atiende una Peticion GET en la ruta "/carrito/finalizar" para
	 * FINALIZAR EL PROCESO DE COMPRA asociado al contenido del Carrito. 
	 * Primero RECUPERAMOS de la SESION del Usuario el contenido del Carrito 
	 * (Puede haber estado comprando previamente y esos articulos en el 
	 * Carrito deben persistirse el tiempo que dure la Sesion).
	 * Si el Carrito esta vacio, redireccionamos a la Pagina Principal.
	 * Si NO esta vacio, Buscamos los Productos del Listado de ID que contiene
	 * el Carrito en la Base de Datos, usando el Metodo productosCarrito(), que
	 * nos devuelve un LISTADO DE OBJETOS Producto.
	 * Insertamos los Productos del Listado de Objetos en un Objeto de Tipo COMPRA,
	 * que primero INICIALIZAMOS asignandole el Usuario que esta Autenticado y
	 * realizando la operacion en ese momento.
	 * Posteriormente vamos ASIGNANDO A CADA PRODUCTO DEL CARRITO LA COMPRA A
	 * LA QUE PERTENECE (Cada Producto tiene un Atributo Objeto Compra a la que 
	 * Pertenece y que hasta este era NULL PORQUE NO EXISTIA COMPRA A LA QUE
	 * VINCULAR EL PRODUCTO) 
	 * El Carrito queda "Vacio" (esto es, cada Producto tiene una Compra asignada
	 * por tanto eliminamos el Carrito de la Sesion).
	 * Finalmente, redireccionamos a la Pagina de la Factura, en la cual se indica
	 * el Identificador de la Compra realizada.
	 * 
	 * @return
	 */

	@GetMapping("/carrito/finalizar")
	public String checkout() {
		List<Long> contenidoCarrito = (List<Long>) session.getAttribute("carrito"); 
		if (contenidoCarrito == null) {
			return "redirect:/public"; 
		}
		List<Producto> productosCarrito = productosCarrito(); 
		Compra miCompra = compraServicio.crearCompra(new Compra(), comprador); 
		for (Producto producto : productosCarrito) {
			compraServicio.addProductoCompra(producto, miCompra); 
		}
		session.removeAttribute("carrito");
		session.removeAttribute("items_carrito");
		return "redirect:/app/mis_compras/factura/" + miCompra.getId();
	}
	
	/**
	 * METODO
	 * ---------------------------------------------------------------------------------------
	 * Metodo que atiende una Peticion GET en la ruta "/mis_compras". Nos muestra
	 * los productos propiedad del Usuario que se encuentra autenticado (Una vez
	 * han sido COMPRADOS).
	 * El metodo redirige a la pagina "/app/compra/compra_list" donde se muestra la 
	 * lista de productos propiedad del Usuario (Una vez COMPRADOS).
	 * 
	 * @param model
	 * @return
	 */
	
	//MOSTRAR EL HISTORICO DE MIS COMPRAS
	@GetMapping("/mis_compras")
	public String verMisCompras(Model model) {
		return "app/compra/compra_list";
	}

	/**
	 * METODO
	 * ---------------------------------------------------------------------------------------
	 * Metodo que atiende una Peticion GET en la ruta "/mis_compras/factura/{id}". 
	 * El PathVariable que se indica es el Identificador de la Compra (Se genera
	 * al Finalizar una Compra).
	 * Primero Recuperamos la Compra de la Base de Datos buscando por su ID.
	 * Despues Recuperamos la Lista de Productos asociados a la Compra.
	 * Recorremos la Lista para calcular el Precio Total de la Compra.
	 * Ahora vamos Inyectando cada uno de los Atributos que queremos mostrar en 
	 * el Model ("productos", "compra" y "total_compra") con su Valor, para 
	 * Mapearlos en la Plantilla Correspondiente (factura.html) y poder 
	 * mostrarlos en la Vista.
	 * Finalmente redirigimos a la Pagina factura.html para mostrarlo.
	 * 
	 * @param model
	 * @param id
	 * @return
	 */

	@GetMapping("/mis_compras/factura/{id}") 
    public String factura(Model model, @PathVariable Long id) {
        Compra miCompra = compraServicio.buscarPorId(id);
        List<Producto> productosCarrito = productoServicio.productosDeUnaCompra(miCompra);
        Float totalCompra = 0.0F;	
		for (Producto producto : productosCarrito) {
			totalCompra += producto.getPrecio();
		}
        model.addAttribute("productos", productosCarrito);
        model.addAttribute("compra", miCompra);
        model.addAttribute("total_compra", totalCompra);
        return "app/compra/factura";
    }
	
	/**
	 * METODO
	 * ---------------------------------------------------------------------------------------
	 * Metodo que atiende una Peticion HTTP usando la anotacion @RequestMapping
	 * (anotacion con cierta antiguedad, de versiones de Spring previas) donde
	 * indica que es de tipo GET en los Parametros. Escucha en la direccion
	 * "/mis_compras/factura/pdf/{id}".
	 * Su funcion es la de generar un Informe en formato PDF, haciendo uso de la 
	 * Libreria iTexT para Java, la cual hemos importado como dependencia Maven en 
	 * el pom.xml.
	 * Para mostrar los datos en el informe, recabamos la informacion que deseamos
	 * haciendo uso de los Servicios especificos que nos proporcionan acceso a las
	 * distintas Entidades. Buscamos la Compra de la que queremos obtener su factura
	 * por su ID, Extraemos una Lista de los Productos que pertenecen a dicha Compra
	 * y calculamos el Importe Total de la Compra.
	 * Haciendo uso de los Metodos del paquete "reports", generamos la factura en PDF.
	 * 
	 * @param id
	 * @return
	 */
	
    @RequestMapping(value = "/mis_compras/factura/pdf/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> facturaPDF(@PathVariable Long id) {
        Compra miCompra = compraServicio.buscarPorId(id);
        List<Producto> productosCarrito = productoServicio.productosDeUnaCompra(miCompra);
        Float totalCarrito = 0.0F;
		if (productosCarrito != null) { 
			for (Producto producto : productosCarrito) {
				totalCarrito += producto.getPrecio();
			}
		}
        ByteArrayInputStream bais = GeneradorPDF.factura2PDF(miCompra, productosCarrito, totalCarrito);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=factura_"+ miCompra.getId()+".pdf");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bais));
    }

    /**
     * METODO
	 * ---------------------------------------------------------------------------------------
	 * 
     * @param id
     * @return
     */
    
    // Saco la factura generada con el servicio, le cambio el PATH
    @RequestMapping(value = "/mis_compras/pdf/factura/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity facturaHTML2PDF(@PathVariable Long id) {

        Compra miCompra = compraServicio.buscarPorId(id);
        List<Producto> productosCarrito = productoServicio.productosDeUnaCompra(miCompra);
        Float totalCarrito = 0.0F;
		if (productosCarrito != null) { 
			for (Producto producto : productosCarrito) {
				totalCarrito += producto.getPrecio();
			}
		}

        Map<String, Object> data = new TreeMap<>();
        String factura = "factura_"+ miCompra.getId();
        data.put("factura", factura);
        data.put("compra", miCompra);
        data.put("productos", productosCarrito);
        data.put("total", totalCarrito);
        data.put("subtotal", (totalCarrito/1.21));
        data.put("iva", (totalCarrito-(totalCarrito/1.21)));

        InputStreamResource resource = documentGeneratorService.html2PdfGenerator(data);
        if (resource != null) {
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        } else {
            return new ResponseEntity(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
	
}

package com.victormoralesperez.wallaspring.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.victormoralesperez.wallaspring.models.Producto;
import com.victormoralesperez.wallaspring.models.Usuario;
import com.victormoralesperez.wallaspring.services.IProductoServicio;
import com.victormoralesperez.wallaspring.services.IUsuarioServicio;
import com.victormoralesperez.wallaspring.storageservice.StorageService;

/**
 * CLASE ProductoController
 * -------------------------------------------------------------------------------------------
 * Clase CONTROLADOR que atiende las Peticiones (Request) y despacha las
 * Respuestas (Response) de todas las funcionalidades relacionadas con la
 * gestion de Productos por parte del Usuario en toda la Zona PRIVADA ("/app") 
 * de la Aplicacion (lo que se puede ver una vez el Usuario esta Registrado y
 * Autenticado).
 * 
 * @author Victor Morales Perez
 *
 */

@Controller
@RequestMapping("/app")
public class ProductoController {

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
	 * Bean de Servicio Auto-Inyectado (Spring con esta anotacion sabe que el Bean
	 * storageService se inyecta como Dependencia en la Clase ProductoController).
	 * Este Servicio nos proporciona los mecanismos necesarios para poder almacenar
	 * las imagenes tanto de los Avatares de los Usuarios como de los Productos en
	 * un fichero de almacenamiento interno del sistema.
	 */

	@Autowired
	StorageService storageService;

	/**
	 * ATRIBUTO
	 * ---------------------------------------------------------------------------------------
	 * Bean POJO Auto-Inyectado (Spring con esta anotacion sabe que el Bean
	 * usuarioPropietario se inyecta como Dependencia en la Clase
	 * ProductoController). Este Objeto Usuario es una Dependencia de la Clase
	 * Producto. representa al Usuario (AUTENTICADO en el momento de realizar 
	 * las operaciones) que tiene en Propiedad el Producto
	 */

	private Usuario usuarioVendedor;

	/**
	 * METODO
	 * ---------------------------------------------------------------------------------------
	 * Los Metodos con la Anotacion @ModelAttribute inyectan en el Model el VALOR
	 * que devuelven, indicando una CLAVE (Model es un MAP). Dicha CLAVE
	 * ("mis_productos") sera mapeada en la VISTA y en ella se podra extraer el
	 * VALOR devuelto por la funcion e inyectarlo en la VISTA como un Atributo del
	 * Model. 
	 * Con este Metodo podemos acceder desde la VISTA a la Lista de Productos que son
	 * propiedad del Usuario que se encuentra Autenticado, ya que los inyecta de 
	 * inicio en el Model.
	 * 
	 * @return
	 */

	@ModelAttribute("mis_productos")
	public List<Producto> misProductos() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		usuarioVendedor = usuarioServicio.buscarPorEMail(email);
		return productoServicio.productosDeUnPropietario(usuarioVendedor);
	}

	/**
	 * METODO
	 * ---------------------------------------------------------------------------------------
	 * Metodo que atiende una Peticion GET en la ruta "/mis_productos". Nos muestra
	 * los productos propiedad del Usuario que se encuentra autenticado. Recibe por
	 * Parametros una Query que no es obligatoria y que si tiene algun valor sera
	 * porque estamos buscando algun producto concreto DE NUESTRA LISTA, y un objeto
	 * Model (Model es un Map que nos permite pasar Objetos del Controlador a la
	 * Vista). Si la Query trae consigo algun valor, se lo inyectamos al Model con
	 * la Clave "mis_productos" y Valor el devuelto por la busqueda realizada por el
	 * Servicio hacia la BD usando el Repositorio. Si lo encuentra, lo muestra. De
	 * no haber ningun producto que se corresponda con el buscado o no existir una
	 * query (no hemos querido buscar nada), la Clave "mis_productos" del Model
	 * seguira mostrando todos los productos. El metodo devuelve un String que es la
	 * ruta de la plantilla html (sin indicar la extension de la misma) que muestra
	 * la Lista de Productos propiedad del Usuario.
	 * 
	 * @param model
	 * @param query
	 * @return
	 */

	@GetMapping("/mis_productos")
	public String list(Model model, @RequestParam(name = "q", required = false) String query) {
		if (query != null) {
			model.addAttribute("mis_productos", productoServicio.buscarMisProductos(query, usuarioVendedor));
		}
		return "app/producto/producto_list";
	}

	/**
	 * METODO
	 * ---------------------------------------------------------------------------------------
	 * Metodo que atiende una Peticion GET en la ruta "/mis_productos/nuevo" para
	 * INSERTAR un Nuevo Producto en el Sistema. Se inyecta en el Model un Command
	 * Object, que es el Bean que recogera la informacion que se introduzca en el
	 * Formulario. El Command Object tendra tantos atributos como campos tenga el
	 * formulario, siendo generalmente una Clase POJO (simple, solamente atributos,
	 * contructor, getters, setters...) que, en este caso, servira posteriormente
	 * para Mapear una Entidad de la Base de Datos (Producto). Es por ello que el
	 * Command Object sera un Objeto Vacio de la Clase Producto del Modelo. El
	 * Command Object se Mapea en la Vista en la plantilla correspondiente (En este
	 * caso en la plantilla producto_form.html) a través de la Clave del Model (recordemos
	 * que es un Map Clave-Valor) "producto" y se accedera en la propia Vista a los
	 * Atributos del mismo usando Thymeleaf (ver comentarios en producto_form.html).
	 * Finalmente el metodo devuelve la URL "app/producto/form" a mostrar en el
	 * Navegador donde cargara la plantilla asociada a dicha URL (producto_form.html).
	 * 
	 * @param model
	 * @return
	 */

	@GetMapping("/mis_productos/nuevo")
	public String nuevoProductoForm(Model model) {
		model.addAttribute("producto", new Producto());
		return "app/producto/producto_form";
	}

	/**
	 * METODO
	 * ---------------------------------------------------------------------------------------
	 * Metodo que atiende una peticion POST en la ruta "/misproducto/nuevo/submit"
	 * la cual se produce al pulsar sobre el Boton Enviar de la plantilla producto_form.html.
	 * El Metodo recoge el Atributo producto del Model que contiene los datos del 
	 * Formulario de la plantilla producto_form.html.
	 * Por otro lado, al incluir una imagen, recibimos un mensaje Multipart de HTTP,
	 * el cual gestionamos en el Controlador con la Clase MultipartFile, que posee
	 * los metodos convenientes para permitirnos procesar la parte de la peticion
	 * que contiene el Fichero y que viene enlazado en la Vista con el nombre "file"
	 * como RequestParam, lo Mapeamos en el Controlador pasandolo como Parametro.
	 * Tambien incluimos el mecanismo de Validacion de datos provenientes del 
	 * Formulario. Si no se ajustan a las restricciones del Objeto POJO que mapean, 
	 * se devuelve a la vista del formulario (pudiendo inlcuir un mensaje de error
	 * informativo al usuario que cumplimenta el formulario).
	 * Una vez validado el contenido del Formulario, mapeamos dicho contenido sobre
	 * un Objeto Entidad que insertaremos posteriormente en la Base de Datos haciendo
	 * uso de los Servicios y Repositorios correspondientes.
	 * 
	 * @param producto
	 * @param file
	 * @return
	 */

	@PostMapping("/mis_productos/nuevo/submit")
	public String nuevoProductoSubmit(@Valid @ModelAttribute Producto producto, BindingResult bindingResult, @RequestParam("file") MultipartFile file) {
		// Validacion del Contenido del Formulario
		if (bindingResult.hasErrors()) { 						//...Si contiene Errores...
			return "app/producto/producto_form"; 						//...Redirijo a la producto_form.
		} else {						 						//...Si todo es Correcto...
			// ...Subimos la Imagen
			if (!file.isEmpty()) {								//...Si el Fichero no esta Vacio
				String imagen = storageService.store(file);
				producto.setImagen(MvcUriComponentsBuilder.fromMethodName(FilesController.class, "serveFile", imagen).build().toUriString());
			}	
			//...Exista o no fichero de imagen asociado al Producto, lo insertaremos a continuacion... (Cuidado con poner ELSE que la vas a cagar pero bien... reflexiona acerca de sus consecuencias...)
			producto.setVendedor(usuarioVendedor); 				// Indicamos el Usuario Vendedor del Producto a insertar (No se recibe desde el Formulario)
			productoServicio.insertar(producto); 				// Finalmente Insertamos
			return "redirect:/app/mis_productos"; 				// Redirige
		}
	}
	
	/**
	 * METODO
	 * ---------------------------------------------------------------------------------------
	 * Metodo que atiende una Peticion GET en la ruta "/mis_productos/editar/{id}" 
	 * para que un Usuario Registrado y Autenticado pueda EDITAR un Producto Existente 
	 * (que sea de su propiedad) en el Sistema. Recibe una Variable {id}, que es
	 * el ID del Producto a Editar. 
	 * Se Busca el Producto en la Base de Datos usando los respectivos Repositorios 
	 * y Servicios. Si el Producto NO EXISTE en la Base de Datos, se redirige a la
	 * Zona Privada de Productos del Usuario.
	 * Si el Producto Existe, se inyecta en el Model un Command Object que contiene
	 * el Objeto Producto recuperado de la Base de Datos y se redirige a la VISTA
	 * de la producto_form del Producto para editar los Atributos del Objeto.
	 * El Command Object se Mapea en la Vista en la plantilla correspondiente (En 
	 * este caso en la plantilla producto_form.html) a través de la Clave del Model 
	 * (recordemos que es un Map Clave-Valor) "producto" y se accedera en la propia 
	 * Vista a los Atributos del mismo usando Thymeleaf (ver comentarios en producto_form.html).
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	
    @GetMapping("/mis_productos/editar/{id}")
    public String editarProducto(@PathVariable Long id, Model model) {
        Producto p = productoServicio.findById(id);
        if (p != null) {
            model.addAttribute("producto", p);
            return "app/producto/producto_form";
        } else {
            return "redirect:/app/mis_productos";
        }
    }
    
    /**
     * METODO 
	 * ---------------------------------------------------------------------------------------
	 * Metodo que atiende una peticion POST en la ruta "/mis_productos/editar/{id}"
	 * la cual se produce al pulsar sobre el Boton Enviar de la plantilla producto_form.html.
	 * El Metodo recoge el Atributo producto del Model que contiene los datos del 
	 * Formulario de la plantilla producto_form.html.
	 * Por otro lado, al incluir una imagen, recibimos un mensaje Multipart de HTTP,
	 * el cual gestionamos en el Controlador con la Clase MultipartFile, que posee
	 * los metodos convenientes para permitirnos procesar la parte de la peticion
	 * que contiene el Fichero y que viene enlazado en la Vista con el nombre "file"
	 * como RequestParam, lo Mapeamos en el Controlador pasandolo como Parametro.
	 * Tambien incluimos el mecanismo de Validacion de datos provenientes del 
	 * Formulario. Si no se ajustan a las restricciones del Objeto POJO que mapean, 
	 * se devuelve a la vista del formulario (pudiendo inlcuir un mensaje de error
	 * informativo al usuario que cumplimenta el formulario).
	 * Por lo demas, el funcionamiento de este metodo es similar al de registrar un
	 * Producto Nuevo, pero evaluamos si el producto existe previamente. Si existe, 
	 * SOBREESCRIBIMOS el Producto con los datos nuevos recibidos del Formulario.
	 * Si NO Existe, REDIRIGIMOS a la pagina de los Productos del Usuario (ESTO
	 * ES IMPORTANTE, PORQUE SI NO EXISTE Y LO INSERTAMOS ESTARIAMOS COMETIENDO
	 * UN ERROR SEMANTICO, YA QUE ESA FUNCIONALIDAD ES CREAR UN PRODUCTO NUEVO Y 
	 * NO ES LA TAREA DE ESTE METODO).
	 * Finalmente redirige a la pagina de los productos asociados al Usuario.
	 * 
     * @param productoEditado
     * @param file
     * @param bindingResult
     * @return
     */
    
    @PostMapping("/mis_productos/editar/submit")
    public String editarProductoSubmit(@Valid @ModelAttribute("producto") Producto productoEditado, BindingResult bindingResult, @RequestParam("file") MultipartFile file) {
        // Si el Formulario tiene errores
        if (bindingResult.hasErrors()) {
            return "app/producto/producto_form";
        } else {
            // Buscamos el antiguo producto para sacar los datos (CREAMOS UNA COPIA PARA TRABAJAR CON ELLA)
            Producto p = productoServicio.findById(productoEditado.getId());
            // Actualizamos el Usuario Vendedor porque es el único campo 
            // que no le hemos podido pasar al Formulario.
            productoEditado.setVendedor(p.getVendedor());
            // Si la imagen no se modifica, la reasignamos
            if(p.getImagen() != null) {
            	productoEditado.setImagen(p.getImagen()); //COMENTAR EN PRUEBAS, DA NULL POINTER EXCEPTION
            }
            // Si me han enviado el fichero con una imagen
            // sera porque la quieren Editar y cambiar
            if (!file.isEmpty()) {
                // Borramos la antigua imagen en nuestro servicio de almacenamiento interno
                if(p.getImagen() != null) {
                	storageService.delete(p.getImagen()); //COMENTAR EN PRUEBAS, DA NULL POINTER EXCEPTION
                }
                // Subimos la nueva al servicio de almacenamiento interno
                String imagen = storageService.store(file);
                productoEditado.setImagen(MvcUriComponentsBuilder.fromMethodName(FilesController.class, "serveFile", imagen).build().toUriString());
            }
            // Actualizamos el producto
            productoServicio.editar(productoEditado);
            // Redirigimos a la pagina de los Productos del Usuario
            return "redirect:/app/mis_productos";
        }
    }

	/**
	 * METODO
	 * ---------------------------------------------------------------------------------------
	 * Metodo que atiende una Peticion GET en la ruta "/mis_productos/{id}/eliminar" 
	 * junto con una Variable {id} en la URL que sera pasanda por parametros con la
	 * anotacion @PathVariable y el tipo de dato de la misma y que se usara para
	 * realizar una busqueda y posterior ELIMINACION de un Producto concreto. 
	 * Primero Busca el Producto, haciendo uso del Servicio de Productos, si lo
	 * encuentra, comprueba que NO tiene ninguna COMPRA asociada (Si yo he subido
	 * un Producto y lo vendo, ya no lo puedo Editar (en este caso la edicion es
	 * Borrarlo)).
	 * En caso de encontrar el producto con el ID asociado, haciendo uso del Servicio
	 * que a su vez usa el Repositorio, lo elimina de la Base de Datos (Borrando 
	 * previamente el Servicio, la imagen asociada, si la tiene).
	 * Finalmente redirige a la pagina de los productos asociados al Usuario.
	 * 
	 * @param id
	 * @return
	 */

	@GetMapping("/mis_productos/{id}/eliminar")
	public String eliminar(@PathVariable Long id) {
		Producto producto = productoServicio.findById(id);
		if (producto.getCompra() == null) { // SI EL PRODUCTO NO ESTA COMPRADO YA POR OTRO CLIENTE
			productoServicio.borrar(producto);
		}
		return "redirect:/app/mis_productos";
	}

}

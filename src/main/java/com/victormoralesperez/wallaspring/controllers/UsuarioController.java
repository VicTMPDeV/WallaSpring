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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import com.victormoralesperez.wallaspring.models.Compra;
import com.victormoralesperez.wallaspring.models.Producto;
import com.victormoralesperez.wallaspring.models.Usuario;
import com.victormoralesperez.wallaspring.services.ICompraServicio;
import com.victormoralesperez.wallaspring.services.IProductoServicio;
import com.victormoralesperez.wallaspring.services.IUsuarioServicio;
import com.victormoralesperez.wallaspring.storageservice.StorageService;

/**
 * CLASE UsuarioController
 * -------------------------------------------------------------------------------------------
 * Clase CONTROLADOR que atiende las Peticiones (Request) y despacha las
 * Respuestas (Response) de todas las funcionalidades relacionadas con la
 * Autenticacion (LOGIN) del Usuario para poder acceder la Zona PRIVADA ("/app") 
 * de la Aplicacion (lo que se puede ver una vez el Usuario esta Registrado y
 * Autenticado).
 * 
 * @author Victor Morales Perez
 *
 */

@Controller
public class UsuarioController {

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

	private Usuario usuario;
	
	/*
	 * ---------------------------------------------------------------------------------------
	 * ZONA PUBLICA
	 * ---------------------------------------------------------------------------------------
	 */
	
	/**
	 * METODO
	 * ---------------------------------------------------------------------------------------
	 * Metodo que conecta la funcionalidad de Registro / Autenticacion
	 * con el Controlador de la Zona Publica.
	 * 
	 * Si no tuvieramos este metodo no podriamos ver ningun producto
	 * aunque tengamos el Public Controller.
	 * 
	 * @return
	 */
	
	@GetMapping("/") // DIRECTORIO RAIZ
	public String wellcome() {
		return "redirect:/public/"; //ACCESO DIRECTO A LA PAGINA WEB, PARA TODO EL MUNDO, SIN AUTENTICAR.
	}
	
	/*
	 * ---------------------------------------------------------------------------------------
	 * ZONA PUBLICA - AUTENTICACION / REGISTRO
	 * ---------------------------------------------------------------------------------------
	 */
	
	/**
	 * METODO
	 * ---------------------------------------------------------------------------------------
	 * Metodo que atiende una Peticion GET en la ruta "/auth/login". Nos muestra
	 * el Formulario de Login para Acceder como Usuario Autenticado a la web.
	 * 
	 * NOTA IMPORTANTE I: Como en la misma URL se implementa el Formulario de 
	 * Registro (Se pasa de uno a otro con una funcionalidad JQuery en la Vista)
	 * aprovechamos y le inyectamos en el Model un Command Object de Usuario vacio, 
	 * que es el Bean que recogera la informacion que se introduzca en el Formulario
	 * de REGISTRO. El Command Object sera Mapeado en la vista a traves de la Clave 
	 * "usuario"
	 * 
	 * NOTA IMPORTANTE II: Deberia existir un Metodo posterior a este, que hiciera
	 * un POST del Login, pero no existe este Metodo porque este Procedimiento
	 * es llevado a cabo por Spring Security.
	 * 
	 * @param model
	 * @return
	 */
	
	@GetMapping("/auth/login") 
	public String login(Model model) {
		model.addAttribute("usuario"); 
		return "login";
	}
	
	/**
	 * METODO
	 * ---------------------------------------------------------------------------------------
	 * Metodo que atiende una Peticion GET en la ruta "/auth/login". Nos muestra
	 * el Formulario de Login para Acceder como Usuario Autenticado a la web.
	 * 
	 * NOTA IMPORTANTE I: Como en la misma URL se implementa el Formulario de 
	 * Registro (Se pasa de uno a otro con una funcionalidad JQuery en la Vista)
	 * aprovechamos y le inyectamos en el Model un Command Object de Usuario vacio, 
	 * que es el Bean que recogera la informacion que se introduzca en el Formulario
	 * de REGISTRO. El Command Object sera Mapeado en la vista a traves de la Clave 
	 * "usuario"
	 * 
	 * NOTA IMPORTANTE II: Deberia existir un Metodo posterior a este, que hiciera
	 * un POST del Login, pero no existe este Metodo porque este Procedimiento
	 * es llevado a cabo por Spring Security.
	 * 
	 * @param model
	 * @return
	 */
	
	@GetMapping("/auth/register") 
	public String register(Model model) {
		model.addAttribute("usuario", new Usuario()); 
		return "register";
	}
	
	/**
	 * METODO
	 * ---------------------------------------------------------------------------------------
	 * Metodo que atiende una Peticion POST en la ruta "/auth/register" en el
	 * caso de que vayamos a Registrar un Nuevo Usuario en la Base de Datos. 
	 * Muestra el Formulario de Registro de Usuario en la aplicacion web.
	 * Se Recoge el Command Object que se habia inyectado en la Peticion Get
	 * del metodo login(), que viene con los datos mapeados en la plantilla login.html
	 * Con los datos del Command Object, se modela la Entidad Usuario registrandolo
	 * posteriormente en la Base de Datos.
	 * Una vez finalizado este proceso, nos redirige al Formulario de Login
	 * para Autenticarnos y crear una Sesion de acceso de este Usuario registrado.
	 * 
	 * @param newUser
	 * @param file
	 * @return
	 */
	
	@PostMapping("/auth/register") 
	public String register(@Valid @ModelAttribute("usuario") Usuario newUser, BindingResult bindingResult, @RequestParam("file") MultipartFile file) {
		// Validacion del Contenido del Formulario
		if(bindingResult.hasErrors()) {
			return "register";	//DEBERIA REDIRIGIR A "register" SI FUERAN FORMULARIOS INDEPENDIENTES, PERO POR HACER MAGIA CON JQUERY NO ENCUENTRO LA MANERA DE HACERLO
		}
		if(!file.isEmpty()) {
			String avatar = storageService.store(file);
			//MONTAMOS LA URL DE LA IMAGEN CON UN PATRON BUILDER EN LUGAR DE HACERLO CONCATENANDO STRINGS
			newUser.setAvatar(MvcUriComponentsBuilder.fromMethodName(FilesController.class, "serveFile", avatar).build().toString());
		}
		usuarioServicio.registrar(newUser);
		return "redirect:/auth/login"; //UNA VEZ SE HA REGISTRADO, LE ENVIAMOS AL LOGIN PARA QUE SE AUTENTIQUE
	}
	
	/*
	 * ---------------------------------------------------------------------------------------
	 * ZONA PRIVADA
	 * ---------------------------------------------------------------------------------------
	 */
	
	/**
	 * METODO
	 * ---------------------------------------------------------------------------------------
	 * Los Metodos con la Anotacion @ModelAttribute inyectan en el Model el VALOR
	 * que devuelven, indicando una CLAVE (Model es un MAP). Dicha CLAVE
	 * ("usuario") sera mapeada en la VISTA y en ella se podra extraer el
	 * VALOR devuelto por la funcion e inyectarlo en la VISTA como un Atributo del
	 * Model. 
	 * Con este Metodo podemos acceder desde la VISTA a los atributos del Usuario
	 * que se encuentra Autenticado, extrayendo su UserName de sus datos de 
	 * Autenticacion.
	 * 
	 * @return
	 */

	@ModelAttribute("misdatos")
	public Usuario misDatos() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		usuario = usuarioServicio.buscarPorEMail(email);
		return usuario;
	}
	
	/**
	 * METODO
	 * ---------------------------------------------------------------------------------------
	 * Metodo que atiende una Peticion GET en la ruta "/app/miperfil". Nos da acceso
	 * a los datos del Usuario Autenticado y ademas inyectamos en el Model el dinero
	 * que ha ganado el Usuario con las Ventas. El metodo devuelve un String que es la
	 * ruta de la plantilla html (sin indicar la extension de la misma) que muestra
	 * la lista de productos propiedad del Usuario.
	 * 
	 * @param model
	 * @param query
	 * @return
	 */

	@GetMapping("/app/miperfil")
	public String list(Model model) {
		List<Producto> misVentas = productoServicio.productosDeUnPropietario(usuario);
		List<Compra> misCompras = compraServicio.buscarTodasPorPropietario(usuario);
		Float totalVentas = 0.0F;
		Float totalCompras = 0.0F;
		//Calculo el Total de mis Ventas
		for (Producto producto : misVentas) {
			if(producto.getCompra() != null) { //...Si el Producto esta Vendido...
				totalVentas += producto.getPrecio();
			}
		}
		//Calculo el Total de mis Compras
		for (Compra compra : misCompras) {
			List<Producto> comprados = productoServicio.productosDeUnaCompra(compra);
			for (Producto comprado : comprados) {
				totalCompras += comprado.getPrecio();
			}
		}
		model.addAttribute("misventas", totalVentas);
		model.addAttribute("miscompras", totalCompras);
		return "app/usuario/perfil";
	}
	
	/**
	 * METODO
	 * ---------------------------------------------------------------------------------------
	 * Metodo que atiende una Peticion GET en la ruta "/misproductos/editar/{id}" 
	 * para que un Usuario Registrado y Autenticado pueda EDITAR sus Datos 
	 * en el Sistema (SIEMPRE QUE LA EDICION DE ESTOS DATOS NO COMPROMETAN LA 
	 * INTEGRIDAD REFERENCIAL DE LA BASE DE DATOS NI INVALIDEN LA SESION ACTUAL,
	 * ELLO IMPLICARIA EN PRIMER TERMINO QUE, SI DESAPARECE UN USUARIO, PERDEMOS
	 * LA TRAZABILIDAD DE EL SUJETO A QUIEN LE COMPRAMOS EL PRODUCTO (Si bien se
	 * puede hacer un "Borrado Logico" en la Base de Datos para mantener esta 
	 * integridad... Se implementara en versiones posteriores por falta de tiempo).
	 * EN SEGUNDO TERMINO IMPLICA QUE SI CAMBIAMOS EL EMAIL DEL USUARIO, LA SESION
	 * ESTA VINCULADO A ESTE CAMPO, TENDRIAMOS QUE SACARLE DE LA SESION Y PEDIRLE
	 * QUE SE AUTENTICARA DE NUEVO, PERDIENDO LOS DATOS ALMACENADOS EN LA SESION
	 * ANTERIOR (Carrito de la Compra)).
	 * Recibe una Variable {id}, que es el ID del Usuario a Editar. 
	 * Se Busca el Usuario en la Base de Datos obteniendo los datos de su Sesion. 
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	
    @GetMapping("/app/miperfil/editar/{id}")
    public String editarUsuario(Model model) { //EN EL MODEL YA ESTA COMO VARIABLE GLOBAR TODA LA INFO DEL USUARIO CON CLAVE "misdatos"
        return "app/usuario/usuarioForm";
    }
    
    /**
     * METODO 
	 * ---------------------------------------------------------------------------------------
	 * Metodo que atiende una peticion POST en la ruta "/misproductos/editar/{id}"
	 * la cual se produce al pulsar sobre el Boton Enviar de la plantilla ficha.html.
	 * El Metodo recoge el Atributo producto del Model que contiene los datos del 
	 * Formulario de la plantilla ficha.html.
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
    
    @PostMapping("/app/miperfil/editar/submit")
    public String editarUsuarioSubmit(@ModelAttribute("misdatos") Usuario usuarioEditado, @RequestParam("file") MultipartFile file) {
            // Buscamos el antiguo Usuario para sacar los datos (CREAMOS UNA COPIA PARA TRABAJAR CON ELLA)
    		Usuario usr = usuarioServicio.findById(usuarioEditado.getId());
    		// Si me han enviado el fichero con una imagen sera porque la quieren Editar y cambiar
            if (!file.isEmpty()) { //Si el fichero NO ESTA VACIO...
                // Subimos la nueva al servicio de almacenamiento interno
                String nuevoAvatar = storageService.store(file);
                usuarioEditado.setAvatar(MvcUriComponentsBuilder.fromMethodName(FilesController.class, "serveFile", nuevoAvatar).build().toUriString());
            }
            // Actualizamos el producto
            usuarioServicio.editar(usuarioEditado);
            // Redirigimos a la pagina de los Productos del Usuario
            return "redirect:/app/miperfil";
    }
    
}

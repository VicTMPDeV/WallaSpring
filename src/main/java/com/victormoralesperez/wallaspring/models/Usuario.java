package com.victormoralesperez.wallaspring.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.victormoralesperez.wallaspring.services.UniqueUsername;

/**
 * CLASE Usuario
 * -------------------------------------------------------------------------------------------
 * Clase POJO (Plain Old Java Object - Una Clase simple que no implementa ni
 * extiende de nada) que Representa la ENTIDAD Usuario de la Base de Datos y que
 * conforma un MODELO de Datos con el que poder trabajar en el Lenguaje de
 * Programacion, una vez salvado el desfase Objeto-Relacional existente entre un
 * Lenguaje orientado a Objetos y el Modelo Relacional de la Base de Datos. El
 * Mapeo Objeto-Relacional que nos proporciona Objetos Java que se corresponden
 * con Entidades de la Base de Datos lo realiza Hybernate usando el estandar JPA
 * (Java Persistance API) y es transparante al Programador.
 * Haremos uso de Lombok, una solución que nos permite evitar tener que escribir 
 * código repetitivo en nuestras Clases. Getters y Setters se reducen a una 
 * única línea de código.
 * 
 * @author Victor Morales Perez
 *
 */

@Entity 										// Anotación de JPA que MAPEA una Clase POJO tratandola como Entidad de la Base  de Datos
@EntityListeners(AuditingEntityListener.class) 	// Auditamos con JPA la Entidad para que funcione correctamente
@Data 											// Anotaciones de LOMBOK : Escriben automaticamente Getters, Setters, Constructores, etc
@NoArgsConstructor 												
public class Usuario {

	/**
	 * ATRIBUTO id
	 * ---------------------------------------------------------------------------------------
	 * Atributo Identificador de la Clase POJO Usuario que representa la Entidad que
	 * se mapea con la Columna Id de la Tabla Usuario de la Base de Datos. Su Valor
	 * es Auto-Generado por JPA. Almacena el identificador del Usuario almacenado en
	 * la Base de Datos. Comienza en el 1 y los valores no son reciclados al
	 * eliminar tuplas de la BD. JPA Trata los Identificadores como tipo de dato
	 * Long por defecto.
	 */

	@Id 												
	@GeneratedValue(strategy = GenerationType.AUTO) 										
	private long id; 									

	/**
	 * ATRIBUTO nombre
	 * ---------------------------------------------------------------------------------------
	 * Atributo nombre de la Clase POJO Usuario que representa la Entidad que se
	 * mapea con la Columna Nombre de la tabla Usuario de la Base de Datos. Almacena
	 * el nombre del Usuario almacenado en la Base de Datos. Usamos la
	 * anotacion @NotEmpty del estándar JSR-303/JSR-380 Bean Validation API, que es
	 * configurado por Hibernate para su implementacion. Esta anotacion nos indica
	 * que el valor no puede ser Nulo.
	 */

	@NotEmpty // Indicamos que NO puede estar vacío este campo (Not Null)
	private String nombre;

	/**
	 * ATRIBUTO apellidos
	 * ---------------------------------------------------------------------------------------
	 * Atributo apellidos de la Clase POJO Usuario que representa la Entidad que se
	 * mapea con la Columna Apellidos de la tabla Usuario de la Base de Datos.
	 * Almacena los apellidos del Usuario almacenado en la Base de Datos. Usamos la
	 * anotacion @NotEmpty del estándar JSR-303/JSR-380 Bean Validation API, que es
	 * configurado por Hibernate para su implementacion. Esta anotacion nos indica
	 * que el valor no puede ser Nulo.
	 */

	@NotEmpty // Indicamos que NO puede estar vacío este campo (Not Null)
	private String apellidos;

	/**
	 * ATRIBUTO avatar
	 * ---------------------------------------------------------------------------------------
	 * Atributo avatar de la Clase POJO Usuario que representa la Entidad que se
	 * mapea con la Columna Avatar de la tabla Usuario de la Base de Datos. 
	 * Almacena la URL donde se encuentra el fichero de imagen asociado al avatar 
	 * del Usuario, por eso es de Tipo String.
	 * Haremos uso de un Servicio Web Externo que, en caso de que el Usuario no 
	 * suba explicitamente una imagen de avatar, le asignara uno auto-generado
	 * que genera en base al email que ha introducido.
	 */

	private String avatar; // Indica la URL donde se ubica la imagen (dentro o fuera de nuestro sistema)

	/**
	 * ATRIBUTO email
	 * ---------------------------------------------------------------------------------------
	 * Atributo email de la Clase POJO Usuario que representa la Entidad que se
	 * mapea con la Columna email de la tabla Usuario de la Base de Datos. Este
	 * atributo es ESPECIALMENTE IMPORTANTE ya que se trata de una CLAVE UNICA, es
	 * decir, no se emplea como Clave Primaria pero es un identificador de segundo
	 * nivel, que por tanto no pueden existir dos iguales y que nos sirve para
	 * IDENTIFICAR UNIVOCAMENTE USUARIOS (De hecho, el usuario se AUTENTICA
	 * introduciendo su EMAIL). Este Atributo se mapea en la Capa de Seguridad
	 * (Autenticacion) como identificador de Usuario. Usamos la anotacion @Email del
	 * estándar JSR-303/JSR-380 Bean Validation API, que es configurado por
	 * Hibernate para su implementacion. Esta anotacion VERIFICA que el Email tiene
	 * un formato correcto y nos ahorra a nosotros tener que implementar un metodo
	 * de validacion de cadena de caracteres con una estructura determinada,
	 * haciendo uso, por ejemplo, de Expresiones Regulares para ello.
	 * Ademas, aseguramos que la direccion de correo electronico no se repite
	 * anyadiendo la restriccion Unique en la estructura DDL de la Base de Datos.
	 * Esto convierte este Atributo en una Clave Alternativa que identifica univocamente
	 * a un Usuario y que utilizaremos en la parte de Seguridad como "UserName" o 
	 * nombre de usuario, para identificar al Usuario que se va a Autenticar.
	 * Esto es asi tambien porque es mas facil a la hora de introducir su nombre
	 * para loguearse que recuerde el email con el que se registro que no que tenga
	 * que memorizar el ID que le asigna el Sistema en la Base de Datos.
	 */
	
	
	@Email(message="{usuario.email.valido}") // Anotacion que verifica la Estructura Valida de un Email
	@Column(unique = true) // Valor Unico en toda la tabla (Clave Unica) 
	@UniqueUsername(message="{usuario.email.exists}") // ANOTACION PROPIA CREADA PARA VALIDAR LA RESTRICCION UNIQUE Y CONTROLAR LA EXCEPCION QUE PRODUCE EN LA BASE DE DATOS
	private String email;

	/**
	 * ATRIBUTO password
	 * ---------------------------------------------------------------------------------------
	 * Atributo password de la Clase POJO Usuario que representa la Entidad que se
	 * mapea con la Columna password de la tabla Usuario de la Base de Datos. Este
	 * atributo es ESPECIALMENTE SENSIBLE, y en la Capa de Seguridad sera CIFRADO
	 * (Encriptado) haciendo uso de la Clase BCryptPasswordEncoder de Spring
	 * Security para cifrar su contenido real al exterior y evitar de este modo
	 * ataques de ciberseguridad con los que pudieran suplantar nuestra identidad y
	 * acceder a nuestra cuenta.
	 */

	private String password; // Este PassWord ira Cifrado

	/**
	 * ATRIBUTO fechaAlta
	 * ---------------------------------------------------------------------------------------
	 * Atributo fechaAlta de la Clase POJO Usuario que representa la Entidad que
	 * se mapea con la Columna fechaAlta de la Tabla Usuario de la Base de Datos. 
	 * Su Valor es Auto-Generado y AUDITADO (Tenemos una Clase de Configuracion en
	 * nuestro paquete .config para que JPA pueda verificar la validez de fechas).
	 */
	
	@CreatedDate 							// Anotacion de Auditoria (JPA) : Auto-Genera la Fecha.
	@Temporal(TemporalType.TIMESTAMP) 		// Al crear la Fecha le asignara la del reloj interno del sistema.
	private Date fechaAlta;

	/**
	 * CONSTRUCTOR PARAMETRIZADO
	 * ---------------------------------------------------------------------------------------
	 * Si bien Lombok genera por nosotros el Constructor por defecto (sin parametros)
	 * usando la anotacion @NoArgsConstructor, a nosotros nos hara falta usar otro 
	 * Constructor tambien al que NO se le pase por Parametros ni el valor del ID ni
	 * el valor de la Fecha de Alta, ya que estos valores JPA los Auto-Genera).
	 * 
	 * @param nombre
	 * @param apellidos
	 * @param avatar
	 * @param email
	 * @param password
	 */
	
	public Usuario(String nombre, String apellidos, String avatar, @Email String email, String password) {
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.avatar = avatar;
		this.email = email;
		this.password = password;
	}

}

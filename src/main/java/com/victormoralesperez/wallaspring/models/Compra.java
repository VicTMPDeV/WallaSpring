package com.victormoralesperez.wallaspring.models;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CLASE Compra
 * -------------------------------------------------------------------------------------------
 * Clase POJO (Plain Old Java Object - Una Clase simple que no implementa ni extiende de nada) 
 * que Representa la ENTIDAD Compra de la Base de Datos y que conforma un MODELO de Datos 
 * con el que poder trabajar en el Lenguaje de Programacion, una vez salvado el desfase 
 * Objeto-Relacional existente entre un Lenguaje orientado a Objetos y el Modelo Relacional 
 * de la Base de Datos.
 * El Mapeo Objeto-Relacional que nos proporciona Objetos Java que se corresponden con Entidades
 * de la Base de Datos lo realiza Hybernate usando el estandar JPA (Java Persistance API) y es
 * transparante al Programador.
 * Haremos uso de Lombok, una solución que nos permite evitar tener que escribir 
 * código repetitivo en nuestras Clases. Getters y Setters se reducen a una 
 * única línea de código.
 * Esta Clase Compra tiene como DEPENDENCIA asociada una instancias de la Clase Usuario
 * Esto es asi porque Cada COMPRA tiene 1 PROPIETARIO (Que lo VENDE o lo COMPRA), que es un
 * Objeto de tipo Usuario.
 * Sera el CONTENEDOR DE INVERSION DE CONTROL el encargado de buscar en el CONTEXTO los BEANS
 * requeridos por esta Clase para su funcionamiento, empleando asi el PATRON DE DISEÑO
 * DE INVERSION DE CONTROL, USANDO INYECCION DE DEPENDENCIAS (Fundamento Esencial de Spring).
 * 
 * @author Victor Morales Perez
 *
 */

@Entity												//Anotación de JPA que MAPEA una Clase POJO tratandola como Entidad de la Base de Datos
@EntityListeners(AuditingEntityListener.class)		//Auditamos con JPA la Entidad para que funcione correctamente
@Data @NoArgsConstructor							//Anotaciones de LOMBOK : Escriben automaticamente Getters, Setters, etc
public class Compra {					
	
	/**
	 * ATRIBUTO id
	 * ---------------------------------------------------------------------------------------
	 * Atributo Identificador de la Clase POJO Producto que representa la Entidad que
	 * se mapea con la Columna Id de la Tabla Compra de la Base de Datos. Su Valor
	 * es Auto-Generado por JPA. Almacena el identificador de una Compra almacenada en
	 * la Base de Datos. Comienza en el 1 y los valores no son reciclados al
	 * eliminar tuplas de la BD. JPA Trata los Identificadores como tipo de dato
	 * Long por defecto.
	 */
	
	@Id												//Clave Primaria de la Entidad
	@GeneratedValue(strategy = GenerationType.AUTO)	//Valor Auto-Generado. Comienza en el 1 y los valores no son reciclados al eliminar tuplas de la BD.
	private long id;								//Los ID son de Tipo Long por defecto en JPA

	/**
	 * ATRIBUTO fechaCompra
	 * ---------------------------------------------------------------------------------------
	 * Atributo fechaCompra de la Clase POJO Compra que representa la Entidad que
	 * se mapea con la Columna fechaCompra de la Tabla Compra de la Base de Datos. 
	 * Su Valor es Auto-Generado y AUDITADO (Tenemos una Clase de Configuracion en
	 * nuestro paquete .config para que JPA pueda verificar la validez de fechas).
	 */
	
	@CreatedDate									//Anotacion de Auditoria (JPA) : Auto-Genera la Fecha.
	@Temporal(TemporalType.TIMESTAMP)				//Al crear la Fecha le asignara la del reloj interno del sistema.
	private Date fechaCompra;
	
	/**
	 * ATRIBUTO propietario
	 * ---------------------------------------------------------------------------------------
	 * Atributo Usuario Comprador de la Clase POJO Compra que REPRESENTA 
	 * LA RELACION que se establece entre la Entidad Usuario y la Entidad Compra.
	 * JPA nos permite Mapear los valores sobre asociaciones entre Entidades, para
	 * ello primero debemos conocer la Multiplicidad de la Relación, que es:
	 * (1 USUARIO puede tener M COMPRAS <-> 1 COMPRA Pertenece SOLO A 1 USUARIO)
	 * De este estudio extraemos la conclusion de que la Relacion es MUCHOS A UNO
	 * y para ello hacemos uso de la Anotacion de JPA @ManyToOne
	 */
	
	@ManyToOne
	private Usuario comprador;	//M COMPRAS tiene 1 USUARIO <-> 1 COMPRA Pertenece SOLO A 1 USUARIO

	
	/**
	 * CONSTRUCTOR PARAMETRIZADO
	 * ---------------------------------------------------------------------------------------
	 * Si bien Lombok genera por nosotros el Constructor por defecto (sin parametros)
	 * usando la anotacion @NoArgsConstructor, a nosotros nos hara falta usar otro 
	 * Constructor al que NO se le pase por Parametros ni el valor del ID ni el de
	 * fechaCompra (JPA los Auto-Genera ambos). 
	 * Observese que el Objeto Usuario que tiene como DEPENDENCIA, no es instanciado en 
	 * ningun momento en esta Clase (Ello implicaria un Alto Acoplamiento, y la violacion
	 * del principio de INVERSION DE CONTROL e INYECCION DE DEPENDENCIAS)
	 * 
	 * @param comprador
	 */

	public Compra(Usuario comprador) { 			
		this.comprador = comprador;
	}
	
}

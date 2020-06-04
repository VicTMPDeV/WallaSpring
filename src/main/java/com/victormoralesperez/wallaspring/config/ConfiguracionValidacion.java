package com.victormoralesperez.wallaspring.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * CLASE ConfiguracionValidacion
 * -------------------------------------------------------------------------------------------
 * Clase de CONFIGURACION de la Aplicacion.
 * Contiene las funciones de Validacion y gestion de Mensajes de Error para todo el sistema.
 * Sirve para filtrar la informacion recibida desde los formularios de la Vista (o el frontend)
 * para dar validez a los datos introducidos por el Usuario desde su navegador.
 * 
 * @author Victor Morales Perez
 *
 */

@Configuration
public class ConfiguracionValidacion {

	/**
	 * METODO QUE PROVEE LA FUENTE DE MENSAJES DEL SISTEMA
	 * --------------------------------------------------------------------------------------
	 * Metodo que maneja los Mensajes de Error y Validacion.
	 * Es la fuente de donde se obtienen los mensajes.
	 * Este Bean nos va a permitir Cargar un Recurso (Fichero) como un Bundle
	 * (Paquete que puede contener cualquier cosa). Nos va a permitir a su vez
	 * establecer donde buscarlo (Ruta) y establer la codificacion por defecto
	 * del fichero erros.properties.
	 * 
	 * @return
	 */

    @Bean
    public MessageSource messageSource() {	
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:errors"); //INDICAMOS DONDE DEBE BUSCAR EL FICHERO DE PROPERTIES
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    /**
     * METODO VALIDADOR
	 * --------------------------------------------------------------------------------------
     * Metodo que establece la manera de Validar los Datos
     * Y la Fuente donde se encuentran los mensajes.
     * Este otro Bean se encarga de Crear el Validador y que le asignemos
     * como ruta el Bean anterior.
     * 
     * @return
     */
    
    @Bean
    public LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource()); //INDICAMOS QUE USE EL MessageSource COMO FUENTE DE MENSAJES A MOSTRAR
        return bean;
    }

}
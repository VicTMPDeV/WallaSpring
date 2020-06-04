package com.victormoralesperez.wallaspring.storageservice;

import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix="storage")
@Getter @Setter
public class StorageProperties {

	/**
	 * ATRIBUTO
	 * ---------------------------------------------------------------------------------------
	 * Nombre del DIRECTORIO ("upload-dir") donde se Almacenaran los Archivos rebidios 
	 * del Multipart (imagenes de Productos y Avatares de Usuarios)
	 */

    private String location = "upload-dir";

}

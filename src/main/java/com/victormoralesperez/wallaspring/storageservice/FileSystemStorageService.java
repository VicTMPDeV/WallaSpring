package com.victormoralesperez.wallaspring.storageservice;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * Implementación de un StorageService que almacena los ficheros subidos
 * dentro del servidor donde se ha desplegado la apliacación.
 * 
 * @author Victor Morales Perez
 *
 */
@Service
public class FileSystemStorageService implements StorageService {

	/**
	 * ATRIBUTO
	 * ---------------------------------------------------------------------------------------
	 * Directorio raiz de nuestro almacén de ficheros
	 * 
	 */

	private final Path rootLocation;

	/**
	 * METODO
	 * ---------------------------------------------------------------------------------------
	 * Metodo que obtiene la Ruta del Directorio Raiz de nuestro almacen de ficheros.
	 * 
	 * @param properties
	 * 
	 */
	
	@Autowired
	public FileSystemStorageService(StorageProperties properties) {
		this.rootLocation = Paths.get(properties.getLocation());
	}

	/**
	 * METODO
	 * ---------------------------------------------------------------------------------------
	 * Metodo que almacena un Fichero en el Sistema de Almacenamiento
	 * secundario desde un objeto de tipo MultipartFile.
	 * 
	 * Implementa un Mecanismo de Seguridad por si 2 o Mas Usuarios
	 * suben un Fichero con el mismo nombre en el mismo instante
	 * (Seria muchisima casualidad que en el mismo Milisegundo ocurriera esto)	
	 * 
	 */
	
	@Override
	public String store(MultipartFile file) {
		String filename = StringUtils.cleanPath(file.getOriginalFilename());
		String extension = StringUtils.getFilenameExtension(filename);
		String justFilename = filename.replace("." + extension, "");
		String storedFilename = System.currentTimeMillis() + "_" + justFilename + "." + extension; //Mecanismo de Seguridad mencionado en la descripcion
		try {
			if (file.isEmpty()) {
				throw new StorageException("ERROR AL TRATAR DE ALMACENAR UN ARCHIVO VACIO: " + filename);
			}
			if (filename.contains("..")) {
				throw new StorageException("NO SE PUEDE ALMACENAR UN ARCHIVO CON UNA RUTA RELATIVA FUERA DEL DIRECTORIO ACTUAL: " + filename);
			}
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, this.rootLocation.resolve(storedFilename), StandardCopyOption.REPLACE_EXISTING);
				return storedFilename;
			}
		} catch (IOException e) {
			throw new StorageException("ERROR AL ALMACENAR EL ARCHIVO: " + filename, e);
		}

	}

	/**
	 * METODO
	 * ---------------------------------------------------------------------------------------
	 * Método que devuelve la ruta de todos los ficheros que hay en el
	 * almacenamiento secundario del proyecto.
	 * 
	 */
	
	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.rootLocation, 1)
						.filter(path -> !path.equals(this.rootLocation))
						.map(this.rootLocation::relativize);
		} catch (IOException e) {
			throw new StorageException("ERROR AL LEER LOS ARCHIVOS ALMACENADOS", e);
		}

	}

	/**
	 * METODO
	 * ---------------------------------------------------------------------------------------
     * Método que es capaz de cargar un fichero a partir de su nombre
     * Devuelve un objeto de tipo Path
     * 
     */
	
    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    
    /**
     * METODO
	 * ---------------------------------------------------------------------------------------
     * Método que es capaz de cargar un fichero a partir de su nombre
     * Devuelve un objeto de tipo Resource
     * 
     */
    
    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    
    /**
     * METODO
	 * ---------------------------------------------------------------------------------------
     * Método que elimina todos los ficheros del almacenamiento
     * secundario del proyecto.
     * 
     */
    
    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    
    /**
     * METODO
	 * ---------------------------------------------------------------------------------------
     * Método que inicializa el almacenamiento secundario del proyecto
     * 
     */
    
    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    /**
     * METODO
	 * ---------------------------------------------------------------------------------------
     * Método BORRA un fichero a partir de su nombre
     * 
     */

	@Override
	public void delete(String filename) {
		String justFilename = StringUtils.getFilename(filename);
		try {
			Path file = load(justFilename);
			Files.deleteIfExists(file);
		} catch (IOException e) {
			throw new StorageException("Error al eliminar un fichero", e);
		}	
	}

}

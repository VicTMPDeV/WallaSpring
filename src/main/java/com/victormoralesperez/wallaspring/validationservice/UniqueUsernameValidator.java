package com.victormoralesperez.wallaspring.validationservice;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.victormoralesperez.wallaspring.repositories.IUsuarioRepositoryDAO;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String>{

	@Autowired
	private IUsuarioRepositoryDAO repositorio;
	
	@Override
	public void initialize(UniqueUsername constraintAnnotation) {
	}
	
	@Override
	public boolean isValid(String email, ConstraintValidatorContext context) {
		if(repositorio == null) { //Si No hay aun Usuarios registrados en el sistema, el repositorio valdra Null y podre insertar ese email.
			return true;
		}
		if(repositorio.findByEmail(email) == null) { // Si la Busqueda en la Base de Datos devuelve Null, es porque NO HAY ningun usuario con ese Email (Username).
			return true;
		}
		return false;
	}

}

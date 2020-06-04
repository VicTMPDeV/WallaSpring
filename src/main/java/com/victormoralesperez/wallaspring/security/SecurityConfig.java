package com.victormoralesperez.wallaspring.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/** EXPLICACION
 * Cada vez que queramos usar la autenticación
 * usara la implementación UserDetailService (UserDetailServiceImpl)
 * que localizará al usuario en la base de datos
 * Lo hara usando la contraseña cifrada por BCrypt
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
    UserDetailsService userDetailsService;	
	
	@Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  //CIFRADOR DE CONTRASEÑAS
    }

	//SOBREESCRIBIMOS LA CONFIGURACIÓN DE LA AUTENTICACIÓN
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
	
	//SOBREESCRIBIMOS LA CONFIGURACIÓN DE LA AUTORIZACIÓN
	 @Override
	    protected void configure(HttpSecurity http) throws Exception {
		 	//PARA INSTANCIAR EL OBJETO HttpSecurity SE EMPLEA EL PATRON BUILDER
	        http      																															
	                .authorizeRequests()																												//AUTORIZO A HACER PETICIONES SIN ESTAR AUTENTICADO...
	                    .antMatchers("/", "/webjars/**","/images/**", "/css/**", "/h2-console/**", "/public/**", "/auth/**", "/files/**").permitAll()		//...EN ESTE CONJUNTO DE RUTAS...
	                    .anyRequest().authenticated()																										//...CUALQUIER OTRA PETICIÓN, DEBE ESTAR AUTENTICADA
	                    .and()																																//...Y ADEMAS...	               																																	
	                .formLogin()																														// ...PARA CUALQUIER OTRA FUNCIONALIDAD HAY QUE LOGUEARSE
	                    .loginPage("/auth/login") 																											//...QUE ESTÁ EN ESTA RUTA...
	                    .defaultSuccessUrl("/public/index", true) 																							//...CUANDO SE PRODUZCA EL LOGUEO CORRECTO, NOS LLEVE POR DEFECTO SIEMPRE (true) A LA PÁGINA...
	                    .loginProcessingUrl("/auth/login-post") 																							//...HACIENDO USO DE ESTE CONTROLADOR
	                    .permitAll()																														//...SE LO PERMITIMOS A TODO EL MUNDO...
	                    .and()																																//...Y ADEMAS...
	                .logout()																															//...AÑADIMOS EL LOGOUT...
	                    .logoutUrl("/auth/logout") 																											//...QUE ESTÁ EN LA URL...
	                    .logoutSuccessUrl("/public/index"); 																								//...Y CUANDO SE PRODUZCA EL LOGOUT, NOS LLEVE AL INDEX

	        //DESABILITAMOS LA SEGURIDAD PARA ACCEDER A LA CONSOLA DE H2
	        http.csrf().disable(); // Se
	        http.headers().frameOptions().disable();
	    }
}

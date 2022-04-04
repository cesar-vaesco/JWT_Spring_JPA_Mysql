package com.vaescode.springboot.app.auth.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * @author cesar
 *
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
		setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/login", "POST"));
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {

		String username = obtainUsername(request);
		String password = obtainPassword(request);

		if (username == null) {
			username = "";
		}
		if (password == null) {
			password = "";
		}

		if (username != null && password != null) {
			logger.info("Username desde request parameter (form-data): " + username);
			logger.info("Password desde request parameter (form-data): " + password);
		}
		username = username.trim();

		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);

		return authenticationManager.authenticate(authToken);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {

		/* Traemos el usuario */
		String username = ((User) authResult.getPrincipal()).getUsername();
		
		/*Obtener roles */
		Collection<? extends GrantedAuthority> roles = authResult.getAuthorities();
		Claims claims = Jwts.claims();
		/*Se agregan los roles a los cleims como un objeto json*/
		claims.put("authorities", new ObjectMapper().writeValueAsString(roles));

		/* Generamos el token */
		SecretKey secretKey = new SecretKeySpec("Alguna.Llave.Secreta.12345Alguna.Llave.Secreta.12345Alguna12345".getBytes(), SignatureAlgorithm.HS512.getJcaName());
		
		
		String token = Jwts.builder()
					   .setClaims(claims)/*se agregan los claims(roles) a el token*/
					   .setSubject(username)
					   .signWith(secretKey)
					   /*Fecha de creación y de expiración*/
					   .setIssuedAt(new Date())
					   .setExpiration(new Date(System.currentTimeMillis() + 14000000L))
					   .compact();

		/* Guardamos el token en la respuesta */
		response.addHeader("Authorization", "Bearer " + token);

		/* Map que va a convertir el contenido en un json */
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("token", token);
		body.put("user", (User) authResult.getPrincipal());
		body.put("mensaje", String.format("Hola %s, has iniciado sesión con éxito", username));

		/* convertir la respuesta en formato json */
		response.getWriter().write(new ObjectMapper().writeValueAsString(body));
		response.setStatus(200);
		response.setContentType("application/json");

	}

}

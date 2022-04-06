package com.vaescode.springboot.app.auth.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaescode.springboot.app.auth.SimpleGrantedAuthorityMixin;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTServiceImpl implements JWTService {

	@Override
	public String create(Authentication auth) throws IOException {
		/* Traemos el usuario */
		String username = ((User) auth.getPrincipal()).getUsername();

		/* Obtener roles */
		Collection<? extends GrantedAuthority> roles = auth.getAuthorities();
		Claims claims = Jwts.claims();
		/* Se agregan los roles a los cleims como un objeto json */
		claims.put("authorities", new ObjectMapper().writeValueAsString(roles));

		/* Generamos el token */
		SecretKey secretKey = new SecretKeySpec(
				"Alguna.Llave.Secreta.12345Alguna.Llave.Secreta.12345Alguna12345".getBytes(),
				SignatureAlgorithm.HS512.getJcaName());

		String token = Jwts.builder().setClaims(claims)/* se agregan los claims(roles) a el token */
				.setSubject(username).signWith(secretKey)
				/* Fecha de creación y de expiración */
				.setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + 14000000L)).compact();
		return token;
	}

	@Override
	public boolean validate(String token) {

		try {
			getClaims(token);
			return true;

		} catch (JwtException | IllegalArgumentException e) {
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public Claims getClaims(String token) {
		Claims claims = Jwts.parserBuilder()
				.setSigningKey("Alguna.Llave.Secreta.12345Alguna.Llave.Secreta.12345Alguna12345".getBytes()).build()
				.parseClaimsJws(resolve(token)).getBody();
		return claims;
	}

	@Override
	public String getUsername(String token) {
		// TODO Auto-generated method stub
		return getClaims(token).getSubject();
	}

	@Override
	public Collection<? extends GrantedAuthority> getRoles(String token) throws IOException {
		Object roles = getClaims(token).get("authorities");

		Collection<? extends GrantedAuthority> authorities = Arrays
				.asList(new ObjectMapper().addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityMixin.class)
						.readValue(roles.toString().getBytes(), SimpleGrantedAuthority[].class));
		return authorities;
	}

	@Override
	public String resolve(String token) {

		if (token != null && token.startsWith("Bearer ")) {
			return token.replaceAll("Bearer", "");

		}
		return null;
	}

}

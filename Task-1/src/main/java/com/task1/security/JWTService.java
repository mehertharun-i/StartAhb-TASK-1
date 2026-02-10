package com.task1.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTService {
	
	@Value("${jwt.secretKey}")
	private String SecretKeyBase64;
	
	@Value("${jwt.expirationTime}")
	private long jwtExpirationTime;

	@Value("${jwt.refreshTokenExpiration}")
	private long refreshTokenExpiration;

	public String extractUserName(String token) {
		return extractClaims(token, Claims :: getSubject);
	}
	
	public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	public String generateToken(String username) {
		return generateToken(new HashMap<>(), username);
	}
	
	public String generateToken(Map<String, Object> extraClaims, String username) {
		return buildToken(extraClaims, username, jwtExpirationTime);
	}

	public String generateRefreshToken(String username) {
		return buildToken(new HashMap<>(), username, refreshTokenExpiration);
	}
	
	private String buildToken( Map<String, Object> extraClaims, String username, long jwtExpirationTime) {
		return Jwts.builder()
					.claims(extraClaims)
					.subject(username)
					.issuedAt(new Date(System.currentTimeMillis()))
					.expiration(new Date(System.currentTimeMillis() + jwtExpirationTime))
					.signWith(getSignInKey())
					.compact();
	}
	
	public boolean isTokenValid(String token, String username) {
		final String usernameFromToken = extractUserName(token);
		return (usernameFromToken.equals(username)) && !isTokenExpired(token);
	}
	
	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	
	private Date extractExpiration(String token) {
		return extractClaims(token, Claims::getExpiration);
	}
	
	private Claims extractAllClaims(String token) {
		 Claims payload = Jwts.parser()
					.verifyWith(getSignInKey())
					.build()
					.parseSignedClaims(token)
					.getPayload();
		 return payload;
	}
	
	private SecretKey getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SecretKeyBase64);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
}

package com.example.jwtsecurity.config;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.example.jwtsecurity.models.Role;
import com.example.jwtsecurity.models.User;
import com.example.jwtsecurity.service.UserAuthService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtProvider {
	private final String ROLES_KEY="roles";
	private JwtParser parser;
	private String secretKey;
	private long validityInMilliseconds;
	@Autowired
	public JwtProvider(@Value("${jwt.secret}")String secretKey,
			@Value("${jwt.token.validity}") long validityInMilliseconds) {
		this.secretKey=Base64.getEncoder().encodeToString(secretKey.getBytes());
		this.validityInMilliseconds=validityInMilliseconds;
	}
	
	@Autowired
	private UserAuthService uservice;
	
	public User getUser(final String token) {
		String uname=Jwts.parser().setSigningKey(secretKey)
				.parseClaimsJws(token).getBody().getSubject();
		return uservice.loadUserByUsername(uname);
	}

	public Map<String,String> generateToken(String username, HttpServletResponse res) {
		
		Claims claims=Jwts.claims().setSubject(username);
		Map<String,String> token_body=new HashMap<>();
		/*use as parameter if needed else skip it
		 * List<Role> roles=new ArrayList();
		claims.put(ROLES_KEY, roles.stream().map(role -> new SimpleGrantedAuthority(role.getRoleName()))
				.filter(Objects :: nonNull)
				.collect(Collectors.toList()));
				*/
		
		//Build Token
		Date now = new Date();
		String token=Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(now)
				.setExpiration(new Date(now.getTime()+validityInMilliseconds))
				.signWith(SignatureAlgorithm.HS256, secretKey)
				.compact();
		token_body.put("token", "Bearer "+token);
		token_body.put("Expiry", ""+new Date(now.getTime()+validityInMilliseconds));
		res.setHeader("Authorization", "Bearer "+token);
		return token_body;
		
	}

	public boolean validateToken(final String token) {
		try {
			Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			return true;
		}
		catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}
}

package com.example.jwtsecurity.config;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.jwtsecurity.service.UserAuthService;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	 private static final String BEARER = "Bearer";
	@Autowired
	private UserAuthService uservice;
	
	public JwtAuthenticationFilter(UserAuthService uservice) {
        this.uservice = uservice;
    }
	 
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String headerValue=((HttpServletRequest)request).getHeader("Authorization");
		getBearerToken(headerValue).ifPresent(token-> {
            //Pull the Username and Roles from the JWT to construct the user details
			uservice.loadUserByJwtToken(token).ifPresent(userDetails -> {
                //Add the user details (Permissions) to the Context for just this API invocation
                SecurityContextHolder.getContext().setAuthentication(
                        new PreAuthenticatedAuthenticationToken(userDetails, "", userDetails.getAuthorities()));
            });
        });
		
		filterChain.doFilter(request, response);

	}
	
	
	
	 private Optional<String> getBearerToken(String headerVal) {
	        if (headerVal != null && headerVal.startsWith(BEARER)) {
	            return Optional.of(headerVal.replace(BEARER, "").trim());
	        }
	        return Optional.empty();
	    }
	
	
}
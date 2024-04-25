package com.it120p.librarymanagementsystem.security.jwt;

import com.it120p.librarymanagementsystem.security.services.UserDetailsServiceImpl;
import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * The AuthTokenFilter class extends the OncePerRequestFilter class from Spring Security.
 *
 * This class is responsible for filtering incoming HTTP requests and managing the authentication process.
 * It checks for a JWT in the Authorization header of the HTTP request. If a JWT is found and it's valid, the filter sets the authentication in the context.
 *
 * The doFilterInternal method is overridden to define the filtering logic.
 * The parseJwt method is a helper method to extract the JWT from the Authorization header.
 */
public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    /**
     * This method is invoked for every incoming HTTP request.
     * It checks for a JWT in the Authorization header of the request. If a JWT is found and it's valid, the method sets the authentication in the context.
     *
     * @param request the HttpServletRequest which houses the details of the request.
     * @param response the HttpServletResponse which houses the details of the response to be sent.
     * @param filterChain the FilterChain which allows the request to proceed further in the filter chain.
     * @throws ServletException if the request for the GET/POST could not be handled
     * @throws IOException if an input or output exception occurred
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Extract JWT from the Authorization header
        try {
            String jwt = parseJwt(request);
            // Validate JWT
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                // Extract username from JWT
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                // Load user details from the database using the username
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                // Create an authentication object
                // UsernamePasswordAuthenticationToken is used to represent the user's authentication request
                UsernamePasswordAuthenticationToken authentication =
                        // Create a new UsernamePasswordAuthenticationToken with the user details and authorities
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());
                // Set the authentication details
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set the authentication in the SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * This method extracts the JWT from the Authorization header of the request.
     *
     * @param request the HttpServletRequest which houses the details of the request.
     * @return a String that contains the JWT if found, or null if not.
     */
    private String parseJwt(HttpServletRequest request) {
        // Extract the Authorization header from the request
        String headerAuth = request.getHeader("Authorization");

        // Check if the Authorization header is not null and has a Bearer token
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}
package com.it120p.librarymanagementsystem.security.jwt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The AuthEntryPointJwt class implements the AuthenticationEntryPoint interface from Spring Security.
 *
 * This class is marked with the @Component annotation, meaning that it is a candidate for Spring's component scanning to detect and add to the application context.
 *
 * It serves as the entry point for handling authentication exceptions. When an unauthenticated user tries to access a protected route, the commence method is triggered.
 *
 * The commence method logs the authentication exception, sets the HTTP response status to 401 (Unauthorized), and sends a JSON response with details about the error.
 */
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    /**
     * This method is invoked when an unauthenticated user tries to access a protected route.
     *
     * @param request the HttpServletRequest which houses the details of the request.
     * @param response the HttpServletResponse which houses the details of the response to be sent.
     * @param authException the AuthenticationException that triggered the entry point.
     * @throws IOException if an input or output exception occurred
     * @throws ServletException if the request for the GET/POST could not be handled
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        // Log the error
        // Set the response status to 401 (Unauthorized)
        logger.error("Unauthorized error: {}", authException.getMessage());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Create a map to hold the response body
        // Populate the map with the details of the error
        final Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "Unauthorized");
        body.put("message", authException.getMessage());
        body.put("path", request.getServletPath());

        // Convert the map to JSON
        final ObjectMapper mapper = new ObjectMapper();
        // Write the JSON to the response
        mapper.writeValue(response.getOutputStream(), body);
    }

}
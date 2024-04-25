package com.it120p.librarymanagementsystem.security.jwt;

import com.it120p.librarymanagementsystem.security.services.UserDetailsImpl;
import java.security.Key;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * The JwtUtils class is a utility class that provides methods for generating, parsing, and validating JWTs (JSON Web Tokens).
 *
 * This class is marked with the @Component annotation, meaning that it is a candidate for Spring's component scanning to detect and add to the application context.
 *
 * The jwtSecret and jwtExpirationMs values are injected from the application properties file using the @Value annotation.
 *
 * The generateJwtToken method is used to generate a JWT for an authenticated user.
 * The key method is a helper method that generates a signing key for the JWT.
 * The getUserNameFromJwtToken method is used to extract the username from a JWT.
 * The validateJwtToken method is used to validate a JWT.
 */
@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${it120p.app.jwtSecret}")
    private String jwtSecret;

    @Value("${it120p.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    /**
     * Generates a JWT for an authenticated user.
     *
     * @param authentication the Authentication object containing the user's authentication information.
     * @return a String representing the JWT.
     */
    public String generateJwtToken(Authentication authentication) {
        // Get the UserDetailsImpl object from the Authentication object.
        // The UserDetailsImpl is used because it contains the user's username.
        // authentication.getPrincipal() returns an Object, so we need to cast it to UserDetailsImpl.
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        // Build the JWT using the user's username, the current date, the expiration date, and the signing key.
        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Generates a signing key for the JWT.
     *
     * @return a Key that can be used to sign a JWT.
     */
    private Key key() {
        // Decode the JWT secret from Base64.
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    /**
     * Extracts the username from a JWT.
     *
     * @param token the JWT from which to extract the username.
     * @return a String representing the username.
     */
    public String getUserNameFromJwtToken(String token) {
        // Parse the JWT and extract the subject (username) from the claims.
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Validates a JWT.
     *
     * @param authToken the JWT to validate.
     * @return a boolean indicating whether the JWT is valid.
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}
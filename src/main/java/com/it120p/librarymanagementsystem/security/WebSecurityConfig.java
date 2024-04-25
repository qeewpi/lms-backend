package com.it120p.librarymanagementsystem.security;

import com.it120p.librarymanagementsystem.security.jwt.AuthEntryPointJwt;
import com.it120p.librarymanagementsystem.security.jwt.AuthTokenFilter;
import com.it120p.librarymanagementsystem.security.services.UserDetailsServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * The WebSecurityConfig class is annotated with @Configuration, indicating that it is a source of bean definitions.
 * The @EnableMethodSecurity annotation is used to enable method-level security.
 *
 * This class is responsible for the security configuration of the application. It defines the security measures like authentication and authorization rules.
 *
 * The UserDetailsServiceImpl and AuthEntryPointJwt are autowired and used for user details retrieval and authentication entry point respectively.
 *
 * The authenticationJwtTokenFilter, authenticationProvider, authenticationManager, and passwordEncoder methods are defined as beans to be used in the application context.
 *
 * The filterChain method is used to define the security filter chain.
 */
@Configuration
@EnableMethodSecurity
// (securedEnabled = true,
// jsr250Enabled = true,
// prePostEnabled = true) // by default
public class WebSecurityConfig { // extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    /**
     * Defines the AuthTokenFilter bean.
     *
     * The AuthTokenFilter class extends OncePerRequestFilter and
     * overrides the doFilterInternal method to intercept the incoming requests.
     * @return a new instance of AuthTokenFilter.
     */
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

//  @Override
//  public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
//    authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
//  }

    /**
     * Defines the DaoAuthenticationProvider bean.
     *
     * The DaoAuthenticationProvider class is used to authenticate the user.
     * @return a new instance of DaoAuthenticationProvider.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

//  @Bean
//  @Override
//  public AuthenticationManager authenticationManagerBean() throws Exception {
//    return super.authenticationManagerBean();
//  }

    /**
     * Defines the AuthenticationManager bean.
     *
     * The AuthenticationManager is used to authenticate the user by calling
     * the getAuthenticationManager method of the AuthenticationConfiguration object.
     *
     * @param authConfig the AuthenticationConfiguration object.
     * @return the AuthenticationManager.
     * @throws Exception if an error occurs when getting the AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Defines the PasswordEncoder bean.
     *
     * It uses the BCryptPasswordEncoder class to encode the password.
     *
     * @return a new instance of BCryptPasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//  @Override
//  protected void configure(HttpSecurity http) throws Exception {
//    http.cors().and().csrf().disable()
//      .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
//      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//      .authorizeRequests().antMatchers("/api/auth/**").permitAll()
//      .antMatchers("/api/test/**").permitAll()
//      .anyRequest().authenticated();
//
//    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
//  }

    /**
     * Defines the SecurityFilterChain bean.
     *
     * In this method, the HttpSecurity object is used to configure the security filters.
     * The security measures like CSRF protection, exception handling, session management, and authorization rules are defined.
     * auth.requestMatchers is used to define the authorization rules for the requests.
     *
     * CSRF protection is disabled, and the authenticationEntryPoint is set to the unauthorizedHandler.
     *
     * @param http the HttpSecurity object.
     * @return the SecurityFilterChain.
     * @throws Exception if an error occurs when building the SecurityFilterChain.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers("book/download/**").permitAll()
                                .requestMatchers("books").permitAll()
                                .requestMatchers("/api/test/**").permitAll()
                                .anyRequest().authenticated()
                );

        http.authenticationProvider(authenticationProvider());

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

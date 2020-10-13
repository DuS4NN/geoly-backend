package com.geoly.app.config;

import com.geoly.app.config.AuthenticationHandler.CustomAuthenticationFailureHandler;
import com.geoly.app.config.AuthenticationHandler.CustomAuthenticationSuccessHandler;
import com.geoly.app.config.AuthenticationHandler.CustomLogoutSuccessHandler;
import com.google.common.collect.ImmutableList;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.ServletContext;
import javax.servlet.SessionTrackingMode;
import java.util.EnumSet;

@Configuration
@EnableWebSecurity
@EnableAsync
@EnableGlobalAuthentication
@EnableGlobalMethodSecurity(
        jsr250Enabled = true,
        prePostEnabled = true,
        securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    public SecurityConfiguration(ServletContext servletContext){
        servletContext.setSessionTrackingModes(EnumSet.of(SessionTrackingMode.COOKIE));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.cors();

        http.sessionManagement()
                .maximumSessions(1)
                .and()
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            .sessionFixation().migrateSession()
                .and()
            .logout()
                .logoutSuccessHandler(logoutSuccessHandler())
                .invalidateHttpSession(true)
                .deleteCookies("SESSION")
                .and()
            .authorizeRequests()
                .antMatchers("/admin").hasRole("ADMIN")
                .antMatchers("/user").hasAnyRole("ADMIN", "USER")
                .antMatchers("/").permitAll()
                .and()
            .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
            .formLogin()
                .failureHandler(authenticationFailureHandler())
                .successHandler(authenticationSuccessHandler());
        http.csrf().disable();


    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(ImmutableList.of("*"));
        configuration.setAllowedMethods(ImmutableList.of("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(ImmutableList.of("*"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new CustomLogoutSuccessHandler();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(){
        return new CustomAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler(){
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    public Argon2PasswordEncoder argon2PasswordEncoder(){
        return new Argon2PasswordEncoder();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher(){
        return new HttpSessionEventPublisher();
    }
}

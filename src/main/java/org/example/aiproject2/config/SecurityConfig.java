
package org.example.aiproject2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .authorizeHttpRequests(auth -> auth

                        // Public authentication endpoints
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/auth/login",
                                "/api/auth/register"
                        ).permitAll()

                        // Dashboard
                        .requestMatchers("/api/dashboard/**")
                        .hasAnyRole("ADMIN", "DOCTOR", "RECEPTIONIST")

                        // Availability rules must come before /api/doctors/** writes
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/doctors/*/availability",
                                "/api/doctors/*/availability/**",
                                "/api/availability/**"
                        ).hasAnyRole("ADMIN", "DOCTOR", "RECEPTIONIST")

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/doctors/*/availability",
                                "/api/doctors/*/availability/**",
                                "/api/availability/**"
                        ).hasAnyRole("ADMIN", "DOCTOR")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/doctors/*/availability",
                                "/api/doctors/*/availability/**",
                                "/api/availability/**"
                        ).hasAnyRole("ADMIN", "DOCTOR")

                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/doctors/*/availability",
                                "/api/doctors/*/availability/**",
                                "/api/availability/**"
                        ).hasAnyRole("ADMIN", "DOCTOR")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/doctors/*/availability",
                                "/api/doctors/*/availability/**",
                                "/api/availability/**"
                        ).hasAnyRole("ADMIN", "DOCTOR")

                        // Patients: all roles may read
                        .requestMatchers(HttpMethod.GET, "/api/patients/**")
                        .hasAnyRole("ADMIN", "DOCTOR", "RECEPTIONIST")

                        // Patients: Admin and Receptionist may modify
                        .requestMatchers(
                                HttpMethod.POST, "/api/patients/**"
                        ).hasAnyRole("ADMIN", "RECEPTIONIST")

                        .requestMatchers(
                                HttpMethod.PUT, "/api/patients/**"
                        ).hasAnyRole("ADMIN", "RECEPTIONIST")

                        .requestMatchers(
                                HttpMethod.PATCH, "/api/patients/**"
                        ).hasAnyRole("ADMIN", "RECEPTIONIST")

                        .requestMatchers(
                                HttpMethod.DELETE, "/api/patients/**"
                        ).hasAnyRole("ADMIN", "RECEPTIONIST")

                        // Doctor records: all roles may read
                        .requestMatchers(HttpMethod.GET, "/api/doctors/**")
                        .hasAnyRole("ADMIN", "DOCTOR", "RECEPTIONIST")

                        // Only Admin may modify doctor records
                        .requestMatchers(
                                HttpMethod.POST, "/api/doctors/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT, "/api/doctors/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PATCH, "/api/doctors/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE, "/api/doctors/**"
                        ).hasRole("ADMIN")

                        // Appointments: all roles may read
                        .requestMatchers(
                                HttpMethod.GET, "/api/appointments/**"
                        ).hasAnyRole("ADMIN", "DOCTOR", "RECEPTIONIST")

                        // Admin and Receptionist may manage appointments
                        .requestMatchers(
                                HttpMethod.POST, "/api/appointments/**"
                        ).hasAnyRole("ADMIN", "RECEPTIONIST")

                        .requestMatchers(
                                HttpMethod.PUT, "/api/appointments/**"
                        ).hasAnyRole("ADMIN", "RECEPTIONIST")

                        .requestMatchers(
                                HttpMethod.PATCH, "/api/appointments/**"
                        ).hasAnyRole("ADMIN", "RECEPTIONIST")

                        .requestMatchers(
                                HttpMethod.DELETE, "/api/appointments/**"
                        ).hasAnyRole("ADMIN", "RECEPTIONIST")

                        // Every other endpoint requires authentication
                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of("http://localhost:5173")
        );

        configuration.setAllowedMethods(
                List.of(
                        "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
                )
        );

        configuration.setAllowedHeaders(
                List.of("Authorization", "Content-Type", "Accept")
        );

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}

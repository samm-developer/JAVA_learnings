package lesson43;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Lesson focus: protect APIs with HTTP Basic auth.
 *
 * Username: learner
 * Password: secret
 *
 * - GET /api/tasks      → needs login
 * - GET /api/tasks/{id} → public (numeric id)
 * - POST/PUT/DELETE     → needs login
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        // Public: get one task by numeric id
                        .requestMatchers(HttpMethod.GET, "/api/tasks/{id:\\d+}").permitAll()
                        // Protected: get all, create, update, delete, /done, /todo
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
                )
                .httpBasic(Customizer.withDefaults())
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }

    @Bean
    UserDetailsService users() {
        UserDetails user = User.builder()
                .username("learner")
                .password("{noop}secret")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}

package lesson43;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Lesson focus: protect /api/** with HTTP Basic auth.
 *
 * Username: learner
 * Password: secret
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // REST APIs often disable CSRF when using Basic/token auth (not browser forms)
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
                )
                // Browser/curl sends: Authorization: Basic base64(user:pass)
                .httpBasic(Customizer.withDefaults())
                // H2 console uses frames
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }

    @Bean
    UserDetailsService users() {
        UserDetails user = User.builder()
                .username("learner")
                // {noop} = store password as plain text (OK for learning only)
                .password("{noop}secret")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}

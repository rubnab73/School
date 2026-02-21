package com.rubayet.school.config;
import com.rubayet.school.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests

                        .requestMatchers("/", "/signup", "/register", "/login").permitAll()

                        // DEPARTMENT MANAGEMENT - Only TEACHER can manage departments
                        .requestMatchers("/departments", "/departments/new", "/departments/delete/**").hasRole("TEACHER")

                        // STUDENT MANAGEMENT
                        // Only TEACHER can delete students
                        .requestMatchers("/students/delete/**").hasRole("TEACHER")
                        // Teachers cannot create students - only authenticated users (students/admins) can
                        .requestMatchers("/students/new").hasRole("ADMIN")
                        .requestMatchers("/students/edit/**").hasRole("STUDENT")
                        .requestMatchers("/students").authenticated()


                        .requestMatchers("/css/**", "/js/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/students", true)
                        .permitAll()
                )
                .logout((logout) -> logout
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(CustomUserDetailsService userDetailsService) {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userDetailsService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }
}
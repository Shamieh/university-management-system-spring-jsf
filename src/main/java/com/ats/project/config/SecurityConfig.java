package com.ats.project.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Autowired
    private final CustomSuccessHandler successHandler;

    public SecurityConfig(CustomSuccessHandler successHandler) {
        this.successHandler = successHandler;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(
                authorizeRequests -> {

                        authorizeRequests.requestMatchers("/login.xhtml", "/about.xhtml", "/javax.faces.resources/**").permitAll();
                        authorizeRequests.requestMatchers("/admin/**").hasRole("ADMIN");
                        authorizeRequests.requestMatchers("/student/**").hasRole("STUDENT");
                        authorizeRequests.anyRequest().authenticated();
                });

        http.formLogin(form -> form
                .loginPage("/login.xhtml")
                .successHandler(successHandler)
        );
        http.httpBasic(Customizer.withDefaults());
        http.logout(Customizer.withDefaults());

        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(AbstractHttpConfigurer::disable);
        http.headers(AbstractHttpConfigurer::disable);


                return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

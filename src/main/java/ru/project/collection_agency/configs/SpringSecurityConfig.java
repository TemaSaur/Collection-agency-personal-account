package ru.project.collection_agency.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SpringSecurityConfig
{
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((authz) -> authz
                    .requestMatchers("/registration", "/login", "/logout", "/email-verification", "/", "css/**").permitAll()
                    .requestMatchers("/delete/**", "/users/all","contracts/all", "debts/all","/users/{user_id}", "debts/add", "/debts/{debt_id}/repaid").hasRole("ADMIN")
                    .requestMatchers("/home", "/edit-info", "/become-admin","/debts/{debt_id}", "contracts/{contract_id}", "home/contracts", "home/debts").hasRole("USER")
                    .requestMatchers("/create/**").hasAuthority("CREATOR")
                    .anyRequest().authenticated())
            .formLogin(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return NoOpPasswordEncoder.getInstance();
    }
}

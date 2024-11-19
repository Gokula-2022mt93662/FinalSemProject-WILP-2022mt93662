package com.example.NewAuthenticationApplication.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.NewAuthenticationApplication.security.CustomDaoAuthenticationProvider;
import com.example.NewAuthenticationApplication.security.CustomPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SpringSecurity {

    @Autowired
    private final CustomPasswordEncoder customPasswordEncoder;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private CustomDaoAuthenticationProvider customDaoAuthenticationProvider;

    public CustomDaoAuthenticationProvider customDaoAuthenticationProvider() {
        CustomDaoAuthenticationProvider provider = new CustomDaoAuthenticationProvider();
        return provider;
    }

    public SpringSecurity (CustomPasswordEncoder customPasswordEncoder, UserDetailsService userDetailsService, CustomDaoAuthenticationProvider customDaoAuthenticationProvider) {
        this.customPasswordEncoder = customPasswordEncoder;
        this.userDetailsService = userDetailsService;
        this.customDaoAuthenticationProvider = customDaoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests((authorize) ->
                        authorize.requestMatchers("/register/**").permitAll()
                                .requestMatchers("/index").permitAll()
                                .requestMatchers("/users").hasRole("ADMIN")
                ).formLogin(
                        form -> form
                                .loginPage("/index")
                                .loginProcessingUrl("/login")
                                .failureUrl("/login?error")
                                .defaultSuccessUrl("/users", true)
                                .permitAll()
                ).logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .logoutSuccessUrl("/login?logout")
                                .permitAll()
                );
        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(customPasswordEncoder);
        auth.authenticationProvider(customDaoAuthenticationProvider);
    }
}

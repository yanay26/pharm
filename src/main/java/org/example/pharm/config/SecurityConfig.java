package org.example.pharm.config;

import org.example.pharm.model.User;
import org.example.pharm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Конфигурация безопасности приложения.
 * <p>
 * Этот класс настраивает безопасность приложения, включая аутентификацию пользователей,
 * авторизацию, настройку страницы входа и выхода, а также управление доступом на основе ролей.
 * Включает использование кастомного сервиса для работы с пользователями и ролями.
 * </p>
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final UserService userService;

    /**
     * Конструктор, инициализирующий сервис пользователей.
     *
     * @param userService сервис для работы с пользователями
     */
    @Autowired
    public SecurityConfig(@Lazy UserService userService) {
        this.userService = userService;
    }

    /**
     * Бин для кодирования паролей с использованием BCrypt.
     *
     * @return PasswordEncoder, использующий BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Бин для загрузки данных пользователя по имени пользователя.
     * Используется для аутентификации пользователей.
     *
     * @return UserDetailsService, который загружает данные пользователя по имени
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            User user = userService.findByUsername(username);
            if (user != null) {
                return new org.springframework.security.core.userdetails.User(
                        user.getUsername(),
                        user.getPassword(),
                        user.getRoles().stream()
                                .map(role -> new org.springframework.security.core.authority.SimpleGrantedAuthority(role.getName()))
                                .toList());
            }
            throw new RuntimeException("User not found");
        };
    }

    /**
     * Бин для создания менеджера аутентификации с использованием настроенного UserDetailsService
     * и PasswordEncoder.
     *
     * @param http объект HttpSecurity для конфигурации безопасности
     * @return AuthenticationManager для аутентификации пользователей
     * @throws Exception если возникает ошибка при настройке аутентификации
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
        return authBuilder.build();
    }

    /**
     * Бин для настройки фильтрации безопасности.
     * Настройка маршрутов, доступных пользователю в зависимости от ролей.
     * Также включает конфигурацию кастомных страниц входа и выхода.
     *
     * @param http объект HttpSecurity для конфигурации безопасности
     * @return SecurityFilterChain, который управляет безопасностью веб-приложения
     * @throws Exception если возникает ошибка при конфигурации безопасности
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Отключение CSRF для упрощения
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/register", "/login").permitAll() // Страницы входа и регистрации доступны всем
                        .requestMatchers("/users").hasRole("ADMIN") // Доступ к странице /users только для ADMIN
                        .anyRequest().authenticated() // Остальные страницы требуют аутентификации
                )
                .formLogin(form -> form
                        .loginPage("/login") // Кастомная страница логина
                        .defaultSuccessUrl("/") // Перенаправление при успешном входе
                        .failureUrl("/login?error=true") // Перенаправление при ошибке входа
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true") // Перенаправление на логин после выхода
                        .permitAll()
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.sendRedirect("/403"); // Редирект на главную страницу при ошибке доступа
                        })
                );
        return http.build();
    }

    /**
     * Бин для кастомного обработчика успешной аутентификации.
     * Перенаправляет пользователя на главную страницу после успешного входа.
     *
     * @return AuthenticationSuccessHandler для перенаправления на главную страницу после входа
     */
    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return (HttpServletRequest request, HttpServletResponse response,
                org.springframework.security.core.Authentication authentication) -> {
            response.sendRedirect("/"); // После входа перенаправить на главную
        };
    }
}

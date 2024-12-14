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
 * Конфигурация безопасности для проекта аптеки.
 * <p>
 * Этот класс настраивает безопасность веб-приложения, включая аутентификацию, авторизацию,
 * кодирование паролей и обработку входа/выхода пользователей.
 * </p>
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final UserService userService;

    /**
     * Конструктор класса, который инжектит сервис пользователя.
     *
     * @param userService сервис для работы с пользователями
     */
    @Autowired
    public SecurityConfig(@Lazy UserService userService) {
        this.userService = userService;
    }

    /**
     * Бин для кодировщика паролей с использованием BCrypt.
     *
     * @return экземпляр BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Бин для загрузки пользователей по имени. Этот метод используется Spring Security для аутентификации.
     * Он загружает данные пользователя по имени и создает объект UserDetails.
     *
     * @return реализация UserDetailsService для поиска пользователей по имени
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
     * Бин для менеджера аутентификации. Настроен для использования кастомного UserDetailsService и кодировщика паролей.
     *
     * @param http объект конфигурации HttpSecurity
     * @return объект AuthenticationManager для управления аутентификацией
     * @throws Exception в случае ошибки настройки менеджера аутентификации
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
        return authBuilder.build();
    }

    /**
     * Настройки безопасности для приложения, включая доступ к различным URL в зависимости от роли пользователя.
     *
     * @param http объект конфигурации HttpSecurity
     * @return объект SecurityFilterChain для настройки безопасности
     * @throws Exception в случае ошибки настройки безопасности
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Отключение CSRF для упрощения
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**","/register", "/login").permitAll() // Страницы входа и регистрации доступны всем
                        .requestMatchers("/users", "/histogram/data").hasRole("ADMIN") // Доступ к странице /users только для ADMIN
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
     * Обработчик успешного входа в систему.
     * <p>
     * После успешной аутентификации пользователь будет перенаправлен на главную страницу.
     * </p>
     *
     * @return обработчик успешного входа
     */
    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return (HttpServletRequest request, HttpServletResponse response,
                org.springframework.security.core.Authentication authentication) -> {
            response.sendRedirect("/"); // После входа перенаправить на главную
        };
    }
}

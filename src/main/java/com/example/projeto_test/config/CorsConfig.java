package com.example.projeto_test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuração CORS (Cross-Origin Resource Sharing) para permitir requisições
 * das páginas HTML estáticas para a API REST.
 *
 * Esta configuração é essencial porque o frontend (HTML/JS) e o backend (API)
 * estão no mesmo servidor, mas navegadores modernos aplicam políticas CORS
 * mesmo para requisições do mesmo domínio em alguns casos.
 *
 * Configurações aplicadas:
 * - Permite qualquer origem (*) - adequado para desenvolvimento
 * - Permite métodos HTTP: GET, POST, PUT, DELETE, OPTIONS
 * - Permite todos os headers customizados
 * - Permite credenciais (cookies, autenticação)
 * - Cache de preflight requests por 1 hora
 *
 * IMPORTANTE: Em produção, substitua "*" por domínios específicos para segurança.
 *
 * @author Aristocracia do Java
 * @version 1.0
 * @since 2025-01-01
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /**
     * Configura as políticas CORS para todos os endpoints da aplicação.
     *
     * @param registry Registro de configurações CORS do Spring
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Aplica a todos os endpoints
                .allowedOriginPatterns("*")  // Permite qualquer origem (DEV - ajustar em produção)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Métodos permitidos
                .allowedHeaders("*")  // Permite todos os headers
                .allowCredentials(true)  // Permite cookies e autenticação
                .maxAge(3600);  // Cache de preflight requests por 1 hora
    }
}

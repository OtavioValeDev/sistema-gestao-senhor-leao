package com.example.projeto_test.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

/**
 * Configuração de recursos estáticos e roteamento da aplicação web.
 *
 * Esta classe configura como o Spring Boot serve arquivos estáticos (HTML, CSS, JS, imagens)
 * e implementa um fallback para roteamento de páginas.
 *
 * Funcionalidades:
 * - Serve arquivos estáticos da pasta /static/
 * - Implementa fallback para index.html em rotas não encontradas
 * - Permite acesso direto a arquivos HTML (cliente.html, gestao-produtos.html, etc.)
 *
 * Lógica de resolução de recursos:
 * 1. Tenta servir o arquivo solicitado diretamente
 * 2. Se não encontrar, tenta servir index.html (fallback para SPA)
 * 3. Se index.html não existir, retorna null (404)
 *
 * @author Aristocracia do Java
 * @version 1.0
 * @since 2025-01-01
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configura handlers para servir arquivos estáticos e implementar fallback.
     *
     * Esta configuração permite que:
     * - Arquivos estáticos sejam acessados diretamente (ex: /cliente.html)
     * - Rotas não encontradas sejam redirecionadas para index.html (SPA routing)
     * - Imagens e assets sejam servidos corretamente
     *
     * @param registry Registro de handlers de recursos do Spring
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Configurar handler para todos os caminhos
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")  // Pasta de arquivos estáticos
                .resourceChain(true)  // Habilita cache e otimizações
                .addResolver(new PathResourceResolver() {
                    /**
                     * Resolve recursos solicitados com fallback inteligente.
                     *
                     * Lógica:
                     * 1. Tenta encontrar o arquivo solicitado
                     * 2. Se não encontrar, tenta servir index.html (fallback SPA)
                     * 3. Retorna null se nada for encontrado (404)
                     */
                    @Override
                    protected Resource getResource(String resourcePath, Resource location) throws java.io.IOException {
                        // Tentar servir o arquivo solicitado diretamente
                        Resource requestedResource = location.createRelative(resourcePath);

                        // Se o arquivo existe e é legível, servir ele
                        if (requestedResource.exists() && requestedResource.isReadable()) {
                            return requestedResource;
                        }

                        // Fallback: tentar servir index.html para SPA routing
                        // Útil para rotas como /cliente ou /produtos que não são arquivos físicos
                        try {
                            Resource indexResource = new ClassPathResource("/static/index.html");
                            if (indexResource.exists() && indexResource.isReadable()) {
                                return indexResource;
                            }
                        } catch (Exception e) {
                            // Ignorar erro silenciosamente se index.html não existir
                        }

                        // Nenhum recurso encontrado - retornar null (Spring retorna 404)
                        return null;
                    }
                });
    }
}

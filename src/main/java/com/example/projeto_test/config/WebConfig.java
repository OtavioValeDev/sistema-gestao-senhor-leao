package com.example.projeto_test.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Servir arquivos estáticos da pasta static
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .resourceChain(true)
                .addResolver(new PathResourceResolver() {
                    @Override
                    protected Resource getResource(String resourcePath, Resource location) throws java.io.IOException {
                        Resource requestedResource = location.createRelative(resourcePath);

                        // Se o arquivo existe, servir ele
                        if (requestedResource.exists() && requestedResource.isReadable()) {
                            return requestedResource;
                        }

                        // Caso contrário, servir index.html para SPA routing (se existir)
                        try {
                            Resource indexResource = new ClassPathResource("/static/index.html");
                            if (indexResource.exists() && indexResource.isReadable()) {
                                return indexResource;
                            }
                        } catch (Exception e) {
                            // Ignorar erro ao tentar carregar index.html
                        }

                        return null;
                    }
                });
    }
}

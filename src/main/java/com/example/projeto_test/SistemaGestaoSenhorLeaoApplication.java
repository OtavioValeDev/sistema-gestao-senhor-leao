package com.example.projeto_test;
// ↑ Declara que esta classe pertence ao pacote principal do projeto

//   Este pacote contém toda a aplicação Spring Boot

import org.springframework.boot.SpringApplication;
// ↑ Importa a classe SpringApplication do Spring Boot
//   Responsável por inicializar toda a aplicação Spring
import org.springframework.boot.autoconfigure.SpringBootApplication;
// ↑ Importa a anotação @SpringBootApplication
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
// ↑ Imports adicionais para funcionalidades específicas

/**
 * Classe principal da aplicação Spring Boot.
 *
 * Esta classe inicializa todo o sistema de gestão de restaurante,
 * incluindo APIs REST para produtos e recibos, interface web
 * e configuração automática do banco H2.
 *
 * Funcionalidades principais:
 * - CRUD completo de produtos do cardápio
 * - Sistema de recibos com números de chamada aleatórios
 * - Interface web responsiva para gestão
 * - APIs REST documentadas
 *
 * @author Sistema de Gestão de Restaurante
 * @version 1.0
 * @since 2025-01-01
 */
@SpringBootApplication
public class SistemaGestaoSenhorLeaoApplication {

	/**
	 * Ponto de entrada da aplicação Java.
	 *
	 * Inicializa o Spring Boot que automaticamente:
	 * - Configura servidor Tomcat embutido na porta 8080
	 * - Carrega configurações do application.properties
	 * - Escaneia e registra todos os componentes
	 * (@Controller, @Service, @Repository)
	 * - Inicializa banco H2 em memória
	 * - Configura validações e tratamento de exceções
	 *
	 * @param args Argumentos da linha de comando (não utilizados)
	 */
	public static void main(String[] args) {
		// Inicia a aplicação Spring Boot
		// O primeiro parâmetro é a classe principal (usada para configurar o contexto)
		// O segundo parâmetro são os argumentos da linha de comando
		SpringApplication.run(SistemaGestaoSenhorLeaoApplication.class, args);
	}

	/**
	 * Configuração adicional para CORS e endpoints de debug
	 */
	@Configuration // ← Indica que esta classe contém configurações
	static class AdditionalConfig implements WebMvcConfigurer {
		// Classe interna para configurações adicionais
		// Permite customizar aspectos específicos da aplicação
	}
}

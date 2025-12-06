// Declaração do pacote onde esta classe está localizada
// O pacote organiza as classes em uma estrutura hierárquica
package com.example.projeto_test;

// Importa a classe SpringApplication do Spring Boot
// Esta classe é responsável por inicializar e executar a aplicação Spring Boot
import org.springframework.boot.SpringApplication;

// Importa a anotação @SpringBootApplication
// Esta anotação combina três anotações importantes:
// - @Configuration: Indica que a classe contém configurações do Spring
// - @EnableAutoConfiguration: Habilita a configuração automática do Spring Boot
// - @ComponentScan: Habilita a varredura de componentes (controllers, services, etc.)
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal da aplicação Spring Boot
 * 
 * Esta é a classe que inicia toda a aplicação. O Spring Boot procura por uma classe
 * com a anotação @SpringBootApplication e o método main para iniciar o servidor.
 * 
 * O Spring Boot automaticamente:
 * - Configura o servidor embutido (Tomcat por padrão)
 * - Configura o contexto da aplicação
 * - Escaneia e registra todos os componentes (@Controller, @Service, @Repository, etc.)
 * - Configura o banco de dados baseado no application.properties
 */
@SpringBootApplication
public class SistemaGestaoSenhorLeaoApplication {

	/**
	 * Método principal - ponto de entrada da aplicação Java
	 * 
	 * @param args Argumentos passados via linha de comando (não utilizados neste caso)
	 * 
	 * SpringApplication.run() faz o seguinte:
	 * 1. Cria o contexto da aplicação Spring
	 * 2. Carrega todas as configurações do application.properties
	 * 3. Escaneia e registra todos os componentes (@Controller, @Service, etc.)
	 * 4. Inicia o servidor web embutido (Tomcat na porta 8080 por padrão)
	 * 5. A aplicação fica rodando até ser encerrada
	 */
	public static void main(String[] args) {
		// Inicia a aplicação Spring Boot
		// O primeiro parâmetro é a classe principal (usada para configurar o contexto)
		// O segundo parâmetro são os argumentos da linha de comando
		SpringApplication.run(SistemaGestaoSenhorLeaoApplication.class, args);
	}

}

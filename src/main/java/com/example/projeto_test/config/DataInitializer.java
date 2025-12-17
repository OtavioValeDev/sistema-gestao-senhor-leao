package com.example.projeto_test.config;

import com.example.projeto_test.model.Funcionario;
import com.example.projeto_test.model.enums.Turno;
import com.example.projeto_test.repository.FuncionarioRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração para inicialização de dados de exemplo no banco de dados.
 * 
 * Esta classe cria dados de exemplo quando a aplicação é iniciada,
 * facilitando testes e demonstrações do sistema.
 */
@Configuration
public class DataInitializer {

    /**
     * Cria um CommandLineRunner que é executado após a inicialização da aplicação.
     * 
     * Insere um funcionário de exemplo no banco de dados se ainda não existir
     * nenhum.
     * 
     * @param funcionarioRepo Repositório de funcionários
     * @return CommandLineRunner para inicialização de dados
     */
    @Bean
    CommandLineRunner initDatabase(FuncionarioRepo funcionarioRepo) {
        return args -> {
            // Verifica se já existem funcionários cadastrados
            if (funcionarioRepo.count() == 0) {
                // Cria um funcionário de exemplo
                Funcionario funcionarioExemplo = Funcionario.builder()
                        .nome("João Silva")
                        .email("joao.silva@senhorleao.com")
                        .senha("senha123")
                        .turno(Turno.matutino)
                        .salario(2500.00)
                        .build();

                funcionarioRepo.save(funcionarioExemplo);

                System.out.println("✅ Funcionário de exemplo criado com sucesso!");
                System.out.println("   Nome: " + funcionarioExemplo.getNome());
                System.out.println("   Email: " + funcionarioExemplo.getEmail());
                System.out.println("   Turno: " + funcionarioExemplo.getTurno());
                System.out.println("   Salário: R$ " + String.format("%.2f", funcionarioExemplo.getSalario()));
            } else {
                System.out.println(
                        "ℹ️  Funcionários já existem no banco de dados. Nenhum dado de exemplo foi adicionado.");
            }
        };
    }
}

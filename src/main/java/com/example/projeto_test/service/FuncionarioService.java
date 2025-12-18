package com.example.projeto_test.service;

import com.example.projeto_test.exception.EmailAlreadyRegisteredException;
import com.example.projeto_test.model.Funcionario;
import com.example.projeto_test.repository.FuncionarioRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Serviço responsável pela lógica de negócios relacionada a Funcionários.
 */
@Service
@RequiredArgsConstructor
public class FuncionarioService {

    private final FuncionarioRepo funcionarioRepo;

    /**
     * Cria um novo funcionário no sistema.
     *
     * @param funcionario O funcionário a ser criado.
     * @throws EmailAlreadyRegisteredException Se o email já estiver cadastrado.
     */
    public void create(Funcionario funcionario) {
        try {
            funcionarioRepo.save(funcionario);
        } catch (DataIntegrityViolationException e) {
            throw new EmailAlreadyRegisteredException("Email already registered");
        }
    }

    /**
     * Busca um funcionário pelo ID.
     *
     * @param id O ID do funcionário.
     * @return O funcionário encontrado.
     * @throws RuntimeException Se o funcionário não for encontrado.
     */
    public Funcionario findById(long id) {
        return funcionarioRepo.findById(id).orElseThrow(() -> new RuntimeException("Employee Not Found"));
    }

    /**
     * Lista todos os funcionários cadastrados.
     *
     * @return Lista de funcionários.
     */
    public List<Funcionario> findAll() {
        return funcionarioRepo.findAll();
    }

    /**
     * Atualiza os dados de um funcionário existente.
     *
     * Este método implementa um merge inteligente: apenas campos não-nulos
     * são atualizados, preservando os valores existentes para campos não informados.
     * Isso permite atualizações parciais sem perder dados já cadastrados.
     *
     * Lógica de negócio (merge inteligente):
     * - Nome: atualiza se não-nulo, senão mantém o atual
     * - Email: atualiza se não-nulo, senão mantém o atual
     * - Turno: atualiza se não-nulo, senão mantém o atual
     * - Senha: atualiza se não-nula, senão mantém a atual
     * - Salário: atualiza se diferente de 0, senão mantém o atual
     *            (0 é usado como indicador de "não informado")
     *
     * IMPORTANTE: O salário usa 0 como valor sentinela para indicar
     * "não atualizar". Se o salário atual for realmente 0, será mantido.
     * Para atualizar para 0, envie explicitamente 0.01 ou valor mínimo.
     *
     * @param funcionario Dados atualizados (campos null são ignorados)
     * @param id          ID do funcionário a ser atualizado
     * @return Funcionário atualizado com merge dos dados antigos e novos
     * @throws RuntimeException Se o funcionário não for encontrado
     */
    public Funcionario update(Funcionario funcionario, long id) {
        // Buscar funcionário existente (lança exceção se não encontrado)
        Funcionario funcionarioAntigo = findById(id);
        
        // Criar novo objeto com merge inteligente usando Builder Pattern
        // Operador ternário: novo_valor != null ? novo_valor : valor_antigo
        Funcionario funcionarioAtualizado = Funcionario.builder()
                .nome(funcionario.getNome() != null ? funcionario.getNome() : funcionarioAntigo.getNome())
                .email(funcionario.getEmail() != null ? funcionario.getEmail() : funcionarioAntigo.getEmail())
                .turno(funcionario.getTurno() != null ? funcionario.getTurno() : funcionarioAntigo.getTurno())
                .senha(funcionario.getSenha() != null ? funcionario.getSenha() : funcionarioAntigo.getSenha())
                .salario(funcionario.getSalario() != 0 ? funcionario.getSalario() : funcionarioAntigo.getSalario())
                .id(funcionarioAntigo.getId())  // Preserva o ID original
                .build();
        
        return funcionarioRepo.save(funcionarioAtualizado);
    }

    /**
     * Remove um funcionário do sistema.
     *
     * @param id O ID do funcionário a ser removido.
     */
    public void deleteById(long id) {
        funcionarioRepo.deleteById(id);
    }

}

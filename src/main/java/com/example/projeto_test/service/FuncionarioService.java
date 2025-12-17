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
     * @param funcionario Dados atualizados.
     * @param id          ID do funcionário a ser atualizado.
     * @return O funcionário atualizado.
     */
    public Funcionario update(Funcionario funcionario, long id) {
        Funcionario funcionarioAntigo = findById(id);
        Funcionario funcionarioAtualizado = Funcionario.builder()
                .nome(funcionario.getNome() != null ? funcionario.getNome() : funcionarioAntigo.getNome())
                .email(funcionario.getEmail() != null ? funcionario.getEmail() : funcionarioAntigo.getEmail())
                .turno(funcionario.getTurno() != null ? funcionario.getTurno() : funcionarioAntigo.getTurno())
                .senha(funcionario.getSenha() != null ? funcionario.getSenha() : funcionarioAntigo.getSenha())
                .salario(funcionario.getSalario() != 0 ? funcionario.getSalario() : funcionarioAntigo.getSalario())
                .id(funcionarioAntigo.getId())
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

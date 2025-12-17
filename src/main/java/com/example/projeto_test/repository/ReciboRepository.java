package com.example.projeto_test.repository;

import com.example.projeto_test.model.Recibo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repositório JPA para a entidade Recibo.
 *
 * Esta interface fornece métodos para acessar e manipular dados
 * de recibos no banco de dados. Herda operações CRUD básicas
 * do JpaRepository e adiciona consultas customizadas.
 *
 * @author Sistema de Gestão de Restaurante
 * @version 1.0
 * @since 2025-01-01
 */
public interface ReciboRepository extends JpaRepository<Recibo, Long> {

    /**
     * Busca um recibo pelo número de chamada.
     *
     * Este método permite consultar um recibo específico
     * usando o número de chamada (notinha) gerado aleatoriamente.
     *
     * @param numeroChamada Número de chamada de 4 dígitos
     * @return Optional contendo o recibo se encontrado
     */
    Optional<Recibo> findByNumeroChamada(String numeroChamada);
}
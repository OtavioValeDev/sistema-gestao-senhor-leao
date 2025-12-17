package com.example.projeto_test.model;

import com.example.projeto_test.model.enums.Turno;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

/**
 * Representa um funcionário do restaurante.
 * 
 * Contém informações pessoais, credenciais de acesso e dados contratuais.
 */
@Entity
@Table(name = "Funcionario")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Funcionario {

    /**
     * Identificador único do funcionário.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome completo do funcionário.
     */
    @Column(nullable = false)
    private String nome;

    /**
     * Endereço de e-mail do funcionário (único no sistema).
     */
    @Column(nullable = false, unique = true)
    @Email
    private String email;

    /**
     * Senha de acesso ao sistema.
     */
    @Column(nullable = false)
    private String senha;

    /**
     * Turno de trabalho do funcionário.
     */
    @Column()
    @Enumerated(EnumType.STRING)
    private Turno turno;

    /**
     * Salário atual do funcionário.
     */
    @Column(nullable = false)
    @PositiveOrZero
    private double salario;
}

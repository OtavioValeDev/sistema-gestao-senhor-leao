package com.example.projeto_test.funcionario.model;

import com.example.projeto_test.funcionario.enuns.Turno;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Entity
@Table(name = "Funcionario")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    @Email
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column()
    @Enumerated(EnumType.STRING)
    private Turno turno;

    @Column(nullable = false)
    @PositiveOrZero
    private double salario;
}

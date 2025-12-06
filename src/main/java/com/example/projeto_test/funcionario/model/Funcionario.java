package com.example.projeto_test.funcionario.model;

import com.example.projeto_test.funcionario.enuns.Turno;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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

    @Column
    private String nome;

    @Column
    @Email
    private String email;

    @Column
    private String senha;

    @Column
    @Enumerated(EnumType.STRING)
    private Turno Turno;
}

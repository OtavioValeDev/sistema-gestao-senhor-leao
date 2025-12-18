package com.example.projeto_test.model.enums;

/**
 * Enum que representa os turnos de trabalho disponíveis para funcionários.
 *
 * Valores possíveis:
 * - matutino: Turno da manhã
 * - vespertino: Turno da tarde
 * - noturno: Turno da noite
 *
 * IMPORTANTE: Os valores são armazenados como STRING no banco de dados
 * (via @Enumerated(EnumType.STRING)), então os nomes devem corresponder
 * exatamente aos valores usados no frontend.
 *
 * @author Aristocracia do Java
 * @version 1.0
 * @since 2025-01-01
 */
public enum Turno {

    /** Turno da manhã */
    matutino,

    /** Turno da tarde */
    vespertino,

    /** Turno da noite */
    noturno

}

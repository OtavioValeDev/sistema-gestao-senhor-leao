package com.example.projeto_test.model;

import java.util.Scanner;

public class Aluno {
    // Atributos
    private String nome;
    private String senha;

    // Construtor vazio
    public Aluno() {
    }

    // Construtor com nome
    public Aluno(String nome) {
        this.nome = nome;
        this.senha = gerarSenha(nome); // Gera a senha automaticamente
    }

    // Setter do nome que também gera a senha
    public void setNome(String nome) {
        this.nome = nome;
        this.senha = gerarSenha(nome);
    }

    // Getter do nome
    public String getNome() {
        return nome;
    }

    // Getter da senha
    public String getSenha() {
        return senha;
    }

    // Método que gera a senha
    private String gerarSenha(String nome) {
        String parteNome = nome.length() >= 3 ? nome.substring(0, 3) : nome;
        int numeroAleatorio = (int) (Math.random() * 900) + 100; // 100 a 999
        return parteNome + numeroAleatorio;
    }

    // Método para apresentar informações do aluno
    public void apresentar() {
        System.out.println("Aluno: " + nome);
        System.out.println("Senha: " + senha);
    }

    // Método main - execução do programa
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Aluno aluno = new Aluno();

        System.out.print("Digite o nome do aluno: ");
        String nomeDigitado = scanner.nextLine();

        aluno.setNome(nomeDigitado); // Define o nome e gera a senha
        aluno.apresentar();

        scanner.close();
    }
}

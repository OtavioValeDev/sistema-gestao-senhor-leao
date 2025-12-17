package com.example.projeto_test.model;
// ↑ Declara que este arquivo pertence ao pacote de modelos (entidades)

import jakarta.persistence.*;
// ↑ Importa todas as anotações JPA para mapeamento objeto-relacional
//   @Entity, @Table, @Id, @Column, etc.
import java.time.LocalDateTime;
// ↑ Importa classe para trabalhar com datas e horas
import java.util.ArrayList;
// ↑ Importa ArrayList para listas dinâmicas
import java.util.List;
// ↑ Importa interface List
import java.util.Random;
// ↑ Importa Random para gerar números aleatórios

/**
 * Entidade que representa um recibo/notinha de venda do restaurante.
 *
 * Esta entidade armazena todas as informações de uma venda realizada,
 * incluindo os itens comprados, total, forma de pagamento e um número
 * de chamada aleatório (similar às notinhas de supermercado).
 *
 * Funcionalidades:
 * - Geração automática de números de chamada (0000-9999)
 * - Cálculo automático de totais
 * - Suporte a atendimento preferencial
 * - Relacionamento com itens de compra
 *
 * @author Sistema de Gestão de Restaurante
 * @version 1.0
 * @since 2025-01-01
 */
@Entity // ← Anotação que marca esta classe como uma entidade JPA
//   - Será mapeada para uma tabela no banco de dados
//   - Campos serão automaticamente persistidos
@Table(name = "recibos") // ← Define o nome da tabela no banco
//   - A tabela será criada como "recibos"
public class Recibo {

    /** Identificador único do recibo no banco de dados */
    @Id // ← Marca este campo como chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // ↑ Estratégia de geração automática de ID
    //   IDENTITY = banco gera automaticamente (AUTO_INCREMENT)
    private Long id;
    // ↑ Campo que armazena o ID único do recibo

    /**
     * Número de chamada aleatório de 4 dígitos.
     * Funciona como uma "notinha" do supermercado, permitindo
     * ao cliente consultar seu pedido posteriormente.
     */
    @Column(unique = true)
    private String numeroChamada;

    /** Data e hora exata da criação/geração do recibo */
    @Column(updatable = false)
    private LocalDateTime dataCriacao;

    /** Observações especiais do cliente (ex: "sem cebola", "entregar na mesa 5") */
    private String observacoes;

    /** Forma de pagamento utilizada (Dinheiro, Cartão, Pix, etc.) */
    private String formaPagamento;

    /** Tipo de atendimento (NORMAL, PREFERENCIAL) */
    private String tipoAtendimento = "NORMAL";

    /** Valor total da venda em centavos (para evitar problemas de arredondamento) */
    private Integer total;

    /**
     * Lista de itens comprados neste recibo.
     * Cada item contém nome, quantidade e preço.
     */
    @ElementCollection
    @CollectionTable(name = "recibo_itens", joinColumns = @JoinColumn(name = "recibo_id"))
    private List<ItemCompra> itens = new ArrayList<>();

    /**
     * Classe interna que representa um item específico dentro de um recibo.
     *
     * Cada item contém as informações de um produto vendido:
     * nome, quantidade comprada e preço unitário.
     */
    @Embeddable
    public static class ItemCompra {

        /** Nome do produto vendido */
        private String nome;

        /** Quantidade comprada deste produto */
        private Integer quantidade;

        /** Preço unitário em centavos */
        private Integer preco;

        /** Construtor padrão necessário para JPA */
        public ItemCompra() {}

        /**
         * Construtor completo para criar um item de compra.
         *
         * @param nome Nome do produto
         * @param quantidade Quantidade comprada
         * @param preco Preço unitário em centavos
         */
        public ItemCompra(String nome, Integer quantidade, Integer preco) {
            this.nome = nome;
            this.quantidade = quantidade;
            this.preco = preco;
        }

        /**
         * Calcula o subtotal deste item (quantidade × preço).
         *
         * @return Subtotal em centavos
         */
        public Integer getSubtotal() {
            return quantidade * preco;
        }

        // Getters e Setters
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        public Integer getQuantidade() { return quantidade; }
        public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
        public Integer getPreco() { return preco; }
        public void setPreco(Integer preco) { this.preco = preco; }
    }

    /** Construtor padrão necessário para JPA */
    public Recibo() {}

    /**
     * Gera/finaliza um recibo com todos os dados necessários.
     *
     * Este método é chamado quando uma venda é finalizada, preenchendo
     * automaticamente a data/hora atual, gerando o número de chamada
     * aleatório e calculando o total da venda.
     *
     * @param itens Lista de itens comprados
     * @param observacoes Observações especiais do cliente
     * @param formaPagamento Forma de pagamento escolhida
     * @param tipoAtendimento Tipo de atendimento (NORMAL ou PREFERENCIAL)
     */
    public void gerarRecibo(List<ItemCompra> itens, String observacoes, String formaPagamento, String tipoAtendimento) {
        // ↑ Método que preenche automaticamente todos os campos do recibo

        this.itens = itens != null ? itens : new ArrayList<>();
        // ↑ Atribui a lista de itens, criando lista vazia se for null
        //   Operador ternário: condição ? valor_se_verdadeiro : valor_se_falso

        this.observacoes = observacoes;
        // ↑ Define as observações especiais do cliente

        this.formaPagamento = formaPagamento;
        // ↑ Define a forma de pagamento escolhida

        this.tipoAtendimento = tipoAtendimento != null ? tipoAtendimento : "NORMAL";
        // ↑ Define o tipo de atendimento, padrão "NORMAL" se não informado

        this.dataCriacao = LocalDateTime.now();
        // ↑ Registra o momento exato da criação do recibo
        //   LocalDateTime.now() = data e hora atuais

        // Gera número de chamada aleatório de 4 dígitos (0000-9999)
        this.numeroChamada = String.format("%04d", new Random().nextInt(10000));
        // ↑ Gera número aleatório e formata como 4 dígitos com zeros à esquerda
        //   nextInt(10000) = número de 0 a 9999
        //   String.format("%04d", numero) = garante 4 dígitos (ex: 0001, 0123, 9999)

        // Calcula o total somando todos os subtotais dos itens
        this.total = this.itens.stream()
                // ↑ Converte a lista em um Stream para operações funcionais
                .mapToInt(ItemCompra::getSubtotal)
                // ↑ Mapeia cada ItemCompra para seu subtotal (quantidade × preço)
                //   :: é referência de método (chama getSubtotal() em cada item)
                .sum();
                // ↑ Soma todos os subtotais resultando no total final
    }

    // ===== GETTERS E SETTERS =====

    /** @return Identificador único do recibo */
    public Long getId() { return id; }

    /** @param id Identificador único do recibo */
    public void setId(Long id) { this.id = id; }

    /** @return Número de chamada de 4 dígitos */
    public String getNumeroChamada() { return numeroChamada; }

    /** @param numeroChamada Número de chamada de 4 dígitos */
    public void setNumeroChamada(String numeroChamada) { this.numeroChamada = numeroChamada; }

    /** @return Data e hora da criação do recibo */
    public LocalDateTime getDataCriacao() { return dataCriacao; }

    /** @param dataCriacao Data e hora da criação do recibo */
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    /** @return Observações especiais do cliente */
    public String getObservacoes() { return observacoes; }

    /** @param observacoes Observações especiais do cliente */
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    /** @return Forma de pagamento utilizada */
    public String getFormaPagamento() { return formaPagamento; }

    /** @param formaPagamento Forma de pagamento utilizada */
    public void setFormaPagamento(String formaPagamento) { this.formaPagamento = formaPagamento; }

    /** @return Tipo de atendimento */
    public String getTipoAtendimento() { return tipoAtendimento; }

    /** @param tipoAtendimento Tipo de atendimento */
    public void setTipoAtendimento(String tipoAtendimento) { this.tipoAtendimento = tipoAtendimento; }

    /** @return Valor total em centavos */
    public Integer getTotal() { return total; }

    /** @param total Valor total em centavos */
    public void setTotal(Integer total) { this.total = total; }

    /** @return Lista de itens comprados */
    public List<ItemCompra> getItens() { return itens; }

    /** @param itens Lista de itens comprados */
    public void setItens(List<ItemCompra> itens) { this.itens = itens; }
}
package com.example.projeto_test.service;
// ↑ Declara que este arquivo pertence ao pacote de serviços

//   Serviços contêm a lógica de negócio da aplicação

import com.example.projeto_test.model.Recibo;
// ↑ Importa a entidade Recibo (representa um pedido/notinha)
import com.example.projeto_test.model.Recibo.ItemCompra;
// ↑ Importa a classe interna ItemCompra (itens dentro de um recibo)
import com.example.projeto_test.repository.ReciboRepository;
// ↑ Importa o repository para acesso aos dados de recibos
import org.springframework.beans.factory.annotation.Autowired;
// ↑ Anotação para injeção automática de dependências
import org.springframework.lang.NonNull;
// ↑ Anotação que indica parâmetros obrigatórios (não null)
import org.springframework.stereotype.Service;
// ↑ Marca esta classe como um serviço do Spring

import java.util.List;
// ↑ Importa interface List para trabalhar com coleções

/**
 * Serviço de negócio para operações com recibos.
 *
 * Esta classe contém toda a lógica de negócio relacionada aos recibos,
 * incluindo geração de números aleatórios, cálculos de total e validações.
 * Atua como intermediário entre o controller e o repository.
 *
 * Funcionalidades principais:
 * - Criação de recibos com números únicos
 * - Validação de carrinhos vazios
 * - Cálculo automático de totais
 * - Geração de números de chamada aleatórios
 *
 * @author Sistema de Gestão de Restaurante
 * @version 1.0
 * @since 2025-01-01
 */
@Service // ← Anotação que registra esta classe como um serviço Spring
// - Instâncias desta classe são gerenciadas pelo Spring
// - Pode ser injetada em outras classes (@Autowired)
public class ReciboService {

    @Autowired // ← Injeção automática do repository
    // Spring encontra automaticamente uma instância de ReciboRepository
    private ReciboRepository reciboRepository;
    // ↑ Campo que armazena referência ao repository de recibos

    /**
     * Cria um novo recibo com itens de compra.
     *
     * Valida se há itens no carrinho, gera automaticamente o número
     * de chamada aleatório e calcula o total da venda.
     *
     * @param itens          Lista de itens comprados
     * @param observacoes    Observações especiais do cliente
     * @param formaPagamento Método de pagamento escolhido
     * @return Recibo criado e salvo no banco
     * @throws IllegalArgumentException Se o carrinho estiver vazio
     */
    public Recibo createRecibo(List<ItemCompra> itens, String observacoes, String formaPagamento) {
        return createRecibo(itens, observacoes, formaPagamento, "NORMAL");
    }

    /**
     * Cria um novo recibo com itens de compra e tipo de atendimento.
     *
     * Valida se há itens no carrinho, gera automaticamente o número
     * de chamada aleatório e calcula o total da venda.
     *
     * @param itens           Lista de itens comprados
     * @param observacoes     Observações especiais do cliente
     * @param formaPagamento  Método de pagamento escolhido
     * @param tipoAtendimento Tipo de atendimento (NORMAL ou PREFERENCIAL)
     * @return Recibo criado e salvo no banco
     * @throws IllegalArgumentException Se o carrinho estiver vazio
     */
    public Recibo createRecibo(List<ItemCompra> itens, String observacoes, String formaPagamento,
            String tipoAtendimento) {
        // ↑ Método principal que cria um novo recibo
        // Recebe todos os dados necessários para gerar um pedido

        if (itens == null || itens.isEmpty()) {
            // ↑ Verifica se a lista de itens é nula ou vazia
            if (!"PREFERENCIAL".equals(tipoAtendimento)) {
                // ↑ Se não é atendimento preferencial, exige itens no carrinho
                throw new IllegalArgumentException("Carrinho não pode estar vazio");
                // ↑ Lança exceção se carrinho estiver vazio para pedidos normais
            }
            // Se é preferencial, permite carrinho vazio (apenas observações)
        }

        Recibo recibo = new Recibo();
        // ↑ Cria uma nova instância da entidade Recibo

        recibo.gerarRecibo(itens, observacoes, formaPagamento, tipoAtendimento);
        // ↑ Chama o método da entidade para preencher todos os dados
        // Este método gera o número aleatório e calcula o total

        return reciboRepository.save(recibo);
        // ↑ Salva o recibo no banco de dados e retorna
        // O save() retorna a entidade salva (com ID gerado)
    }

    /**
     * Retorna todos os recibos cadastrados.
     *
     * @return Lista completa de recibos
     */
    public List<Recibo> getAllRecibos() {
        return reciboRepository.findAll();
    }

    /**
     * Busca um recibo específico pelo ID.
     *
     * @param id Identificador único do recibo
     * @return Recibo encontrado
     * @throws RuntimeException Se o recibo não for encontrado
     */
    public Recibo getReciboById(@NonNull Long id) {
        return reciboRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recibo not found"));
    }

    /**
     * Atualiza as observações, forma de pagamento e itens de um recibo.
     *
     * @param id         ID do recibo a ser atualizado
     * @param novosDados Dados novos
     * @return Recibo atualizado
     */
    public Recibo updateRecibo(@NonNull Long id, Recibo novosDados) {
        Recibo recibo = getReciboById(id);

        if (novosDados.getObservacoes() != null) {
            recibo.setObservacoes(novosDados.getObservacoes());
        }
        if (novosDados.getFormaPagamento() != null) {
            recibo.setFormaPagamento(novosDados.getFormaPagamento());
        }

        // Atualizar itens se fornecidos
        if (novosDados.getItens() != null && !novosDados.getItens().isEmpty()) {
            recibo.setItens(novosDados.getItens());

            // Recalcular total
            int novoTotal = recibo.getItens().stream()
                    .mapToInt(ItemCompra::getSubtotal)
                    .sum();
            recibo.setTotal(novoTotal);
        }

        return reciboRepository.save(recibo);
    }

    /**
     * Busca um recibo pelo número de chamada (notinha).
     *
     * @param numeroChamada Número de 4 dígitos gerado aleatoriamente
     * @return Recibo encontrado
     * @throws RuntimeException Se o recibo não for encontrado
     */
    public Recibo getReciboByNumeroChamada(String numeroChamada) {
        return reciboRepository.findByNumeroChamada(numeroChamada)
                .orElseThrow(() -> new RuntimeException("Recibo not found"));
    }

    /**
     * Remove um recibo específico do sistema.
     *
     * @param id Identificador único do recibo
     */
    public void deleteRecibo(@NonNull Long id) {
        reciboRepository.deleteById(id);
    }

    /**
     * Remove todos os recibos do sistema.
     *
     * Método utilitário para limpar o histórico em ambiente de desenvolvimento.
     */
    public void deleteAllRecibos() {
        reciboRepository.deleteAll();
    }
}
package com.example.projeto_test.controller;
// ↑ Declara que este arquivo pertence ao pacote de controllers
//   Controllers são responsáveis por receber requisições HTTP e devolver respostas

import org.springframework.http.HttpStatus;
// ↑ Importa enum com códigos de status HTTP (200, 201, 404, etc.)
import org.springframework.http.ResponseEntity;
// ↑ Importa classe para criar respostas HTTP estruturadas
import org.springframework.web.bind.annotation.*;
// ↑ Importa todas as anotações para mapeamento de endpoints REST
import org.springframework.web.multipart.MultipartFile;
// ↑ Importa classe para trabalhar com arquivos multipart (upload)

import java.io.IOException;
// ↑ Importa classe para tratamento de exceções de I/O
import java.nio.file.Files;
// ↑ Importa classe para operações com arquivos
import java.nio.file.Path;
// ↑ Importa classe que representa caminhos de arquivos
import java.nio.file.Paths;
// ↑ Importa classe utilitária para criar caminhos
import java.nio.file.StandardCopyOption;
// ↑ Importa opções para cópia de arquivos
import java.util.HashMap;
// ↑ Importa classe HashMap para criar mapas
import java.util.Map;
// ↑ Importa interface Map
import java.util.UUID;
// ↑ Importa classe para gerar IDs únicos

/**
 * Controller REST para gerenciamento de upload de imagens.
 *
 * Esta classe expõe endpoints HTTP para:
 * - Upload de imagens de produtos
 * - Validação de tipos de arquivo
 * - Geração de nomes únicos para arquivos
 * - Gerenciamento de CORS para uploads
 *
 * As imagens são salvas no diretório /uploads/ e podem ser
 * referenciadas pelos produtos do cardápio.
 *
 * @author Aristocracia do Java
 * @version 1.0
 * @since 2025-01-01
 */
@RestController // ← Anotação que marca esta classe como um controller REST
//   - Todas as respostas são automaticamente convertidas para JSON
@RequestMapping("/api/images") // ← Define a URL base para todos os endpoints desta classe
//   - Todos os endpoints começarão com /api/images
public class ImageController {

    // Diretório onde as imagens serão salvas (relativo ao projeto)
    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";
    // ↑ Caminho físico onde os arquivos são salvos no servidor

    // URL base para acessar as imagens via web
    private static final String UPLOAD_URL = "/uploads/";
    // ↑ URL relativa para acessar as imagens no navegador

    /**
     * Construtor da classe.
     *
     * Inicializa o controller criando o diretório de uploads
     * se ele não existir. Isso garante que o sistema possa
     * salvar imagens mesmo na primeira execução.
     */
    public ImageController() {
        // Criar diretório de uploads se não existir
        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            // ↑ Converte string do caminho em objeto Path
            if (!Files.exists(uploadPath)) {
                // ↑ Verifica se o diretório já existe
                Files.createDirectories(uploadPath);
                // ↑ Cria o diretório e subdiretórios necessários
            }
        } catch (IOException e) {
            System.err.println("Erro ao criar diretório de uploads: " + e.getMessage());
        }
    }

    /**
     * Faz upload de uma imagem para o servidor.
     *
     * Recebe um arquivo de imagem via multipart/form-data, valida
     * o tipo de arquivo, gera um nome único para evitar conflitos,
     * salva no diretório de uploads e retorna a URL de acesso.
     *
     * Tipos aceitos: JPEG, PNG, GIF, WebP, etc. (qualquer image/*)
     * Tamanho máximo: Depende da configuração do servidor
     *
     * @param file Arquivo de imagem enviado via formulário
     * @return JSON com URL da imagem e mensagem de sucesso
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            // Validar se o arquivo não está vazio
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Arquivo vazio");
            }

            // Validar tipo de arquivo (apenas imagens)
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest().body("Apenas arquivos de imagem são permitidos");
            }

            // Gerar nome único para o arquivo
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                // ↑ Extrai extensão do arquivo original (.jpg, .png, etc.)
            }
            String uniqueFilename = UUID.randomUUID().toString() + extension;
            // ↑ Gera nome único para evitar conflitos de nomes

            // Salvar arquivo
            Path uploadPath = Paths.get(UPLOAD_DIR);
            Path filePath = uploadPath.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            // ↑ Salva o arquivo no diretório de uploads

            // Retornar URL da imagem
            String imageUrl = UPLOAD_URL + uniqueFilename;
            Map<String, String> response = new HashMap<>();
            response.put("imageUrl", imageUrl);
            response.put("message", "Imagem enviada com sucesso");

            return ResponseEntity.ok()
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                    .header("Access-Control-Allow-Headers", "*")
                    .body(response);

        } catch (IOException e) {
            System.err.println("Erro ao salvar imagem: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao salvar imagem: " + e.getMessage());
        }
    }

    /**
     * Trata requisições OPTIONS para CORS (Cross-Origin Resource Sharing).
     *
     * Este método permite que o frontend (executado em domínio diferente)
     * faça upload de imagens para este servidor, configurando os headers
     * necessários para permitir origem, métodos e headers customizados.
     *
     * @return Resposta vazia com headers CORS configurados
     */
    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> handleOptions() {
        return ResponseEntity.ok()
                .header("Access-Control-Allow-Origin", "*")     // ← Permite qualquer origem
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS") // ← Métodos permitidos
                .header("Access-Control-Allow-Headers", "*")    // ← Headers permitidos
                .build();
    }
}


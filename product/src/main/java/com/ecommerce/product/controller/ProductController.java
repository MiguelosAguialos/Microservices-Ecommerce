package com.ecommerce.product.controller;

import com.ecommerce.product.dto.ProductRequest;
import com.ecommerce.product.dto.ProductResponse;
import com.ecommerce.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
@Tag(name = "Products", description = "Operacoes de catalogo de produtos do e-commerce")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "Listar produtos", description = "Retorna todos os produtos cadastrados, ativos e inativos.")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso"))
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return new ResponseEntity<>(productService.fetchAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/active")
    @Operation(summary = "Listar produtos ativos", description = "Retorna apenas os produtos marcados como ativos para exibicao no catalogo.")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Lista de produtos ativos retornada com sucesso"))
    public ResponseEntity<List<ProductResponse>> getActiveProducts() {
        return new ResponseEntity<>(productService.fetchActiveProducts(), HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Criar produto", description = "Cria um novo item do catalogo com preco, estoque, categoria e imagem.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Produto criado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "400", description = "Requisicao invalida")
    })
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest) {
        return new ResponseEntity<>(productService.createProduct(productRequest), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID", description = "Retorna os detalhes de um produto especifico pelo identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "404", description = "Produto nao encontrado")
    })
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        return productService.fetchProductById(id).map(productResponse -> new ResponseEntity<>(productResponse, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/search")
    @Operation(summary = "Pesquisar produtos", description = "Busca produtos por palavra-chave no nome, descricao ou categoria.")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Pesquisa executada com sucesso"))
    public ResponseEntity<List<ProductResponse>> searchProducts(@RequestParam String keyWord) {
        return new ResponseEntity<>(productService.searchProducts(keyWord), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar produto", description = "Atualiza os dados cadastrais de um produto existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "404", description = "Produto nao encontrado")
    })
    public ResponseEntity<Boolean> updateProduct(@PathVariable Long id, @RequestBody ProductRequest productRequest) {
        return productService.updateProduct(id, productRequest).map(productResponse -> new ResponseEntity<>(productResponse, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover produto", description = "Remove um produto do catalogo pelo seu ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto nao encontrado")
    })
    public ResponseEntity<Boolean> deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id).map(productResponse -> new ResponseEntity<>(productResponse, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}

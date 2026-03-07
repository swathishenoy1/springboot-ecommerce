package com.springtutorial.store.controllers;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.springtutorial.store.dtos.ProductDto;
import com.springtutorial.store.entities.Product;
import com.springtutorial.store.mappers.ProductMapper;
import com.springtutorial.store.repositories.CategoryRepository;
import com.springtutorial.store.repositories.ProductRepository;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @GetMapping
    private Iterable<ProductDto> getAllUsers(@RequestParam (required = false, name = "categoryId") Byte categoryId){
        
        List<Product> products;
        if(categoryId != null){
            products = productRepository.findProductByCategoryId(categoryId);
        }else{
            products = productRepository.findAllWithCategory();
        }
        return products
        .stream()
        .map(productMapper::toDto)
        .toList();
    }

    @GetMapping("/{id}")
    private ResponseEntity<ProductDto> getProduct(@PathVariable Long id) {
        var product = productRepository.findById(id).orElse(null);

        if(product == null){
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(productMapper.toDto(product));
        
    }

    @PostMapping
    public ResponseEntity<ProductDto> createUser( @RequestBody ProductDto productDto,
        UriComponentsBuilder uriComponentsBuilder) 
    {
        var category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);

        if(category == null) {
            return ResponseEntity.badRequest().build();
        }
        
        var product = productMapper.toEntity(productDto);
        product.setCategory(category);
        productRepository.save(product);

        productDto.setId(product.getId());
        var uri = uriComponentsBuilder.path("/products/{id}").buildAndExpand(productDto.getId()).toUri();
        return ResponseEntity.created(uri).body(productDto);

    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(
    @PathVariable(name = "id") Long id, 
    @RequestBody ProductDto productDto) {

        var category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);

        if(category == null) {
            return ResponseEntity.badRequest().build();
        }

        var product = productRepository.findById(id).orElse(null);
        if(product == null){
            return ResponseEntity.notFound().build();
        }
        
        productMapper.updateProduct(productDto, product);
        product.setCategory(category);
        productRepository.save(product);

        productDto.setId(product.getId());

        return ResponseEntity.ok(productDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        
        var product = productRepository.findById(id).orElse(null);
        if(product == null){
            return ResponseEntity.notFound().build();
        }
        productRepository.delete(product); 
        return ResponseEntity.noContent().build();
    }
}

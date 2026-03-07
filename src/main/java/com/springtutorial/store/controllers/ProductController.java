package com.springtutorial.store.controllers;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springtutorial.store.dtos.ProductDto;
import com.springtutorial.store.entities.Product;
import com.springtutorial.store.mappers.ProductMapper;
import com.springtutorial.store.repositories.ProductRepository;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductRepository productRepository;
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
}

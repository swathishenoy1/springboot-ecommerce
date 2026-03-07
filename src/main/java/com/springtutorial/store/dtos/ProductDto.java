package com.springtutorial.store.dtos;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ProductDto {
    @JsonProperty("product_id")
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Byte categoryId;
}

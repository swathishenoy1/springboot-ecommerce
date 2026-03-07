package com.springtutorial.store.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.springtutorial.store.dtos.ProductDto;
import com.springtutorial.store.entities.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source="category.id" , target = "categoryId")
    ProductDto toDto(Product product);
}

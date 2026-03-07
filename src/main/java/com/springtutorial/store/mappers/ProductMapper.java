package com.springtutorial.store.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.springtutorial.store.dtos.ProductDto;
import com.springtutorial.store.dtos.UpdateUserRequest;
import com.springtutorial.store.entities.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source="category.id" , target = "categoryId")
    ProductDto toDto(Product product);
    Product toEntity(ProductDto productDto);
    @Mapping(target="id", ignore=true)
    void updateProduct(ProductDto productDto, @MappingTarget Product product);
}

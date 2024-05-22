package com.ordersmicroservice.orders_microservice.mapper;

import com.ordersmicroservice.orders_microservice.dto.CartProductDto;
import com.ordersmicroservice.orders_microservice.models.OrderedProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductMapper MAP_INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(source = "id", target = "productId")
    @Mapping(source = "productName", target = "name")
    OrderedProduct orderedProductToCartProduct(CartProductDto productDto);

}

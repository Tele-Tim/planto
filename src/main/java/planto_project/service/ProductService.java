package planto_project.service;

import org.springframework.stereotype.Service;
import planto_project.dto.ProductDto;

import java.util.Set;

@Service
public interface ProductService {
    ProductDto createProduct(ProductDto productDto);

    ProductDto getProduct(String id);

    ProductDto updateProduct(String id, ProductDto productDto);

    ProductDto deleteProduct(String id);

    ProductDto changeQuantityOfProduct(String id, int amount, boolean flag);

    Set<ProductDto> findAllProducts();
}

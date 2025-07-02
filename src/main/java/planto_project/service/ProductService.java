package planto_project.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import planto_project.dto.NewProductDto;
import planto_project.dto.ProductDto;
import planto_project.dto.SortingDto;

import java.util.List;
import java.util.Set;

@Service
public interface ProductService {
    ProductDto createProduct(NewProductDto newProductDto);

    ProductDto getProduct(String id);

    ProductDto updateProduct(String id, ProductDto productDto);

    ProductDto deleteProduct(String id);

    ProductDto changeQuantityOfProduct(String id, int amount, boolean flag);

    Page<ProductDto> findAllProducts(SortingDto sortingDto);
}

package planto_project.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import planto_project.dto.filters_dto.DataForProductsFiltersDto;
import planto_project.dto.NewProductDto;
import planto_project.dto.ProductDto;
import planto_project.dto.SortingDto;

@Service
public interface ProductService {
    ProductDto createProduct(NewProductDto newProductDto);

    ProductDto getProduct(String id);

    ProductDto updateProduct(String id, ProductDto productDto);

    ProductDto deleteProduct(String id);

    ProductDto changeQuantityOfProduct(String id, int amount, boolean flag);

    Page<ProductDto> findAllProducts(SortingDto sortingDto);

    Page<ProductDto> findAllProductsWithCriteria(SortingDto sortingDto);

    DataForProductsFiltersDto getDataForFilters();
}

package planto_project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import planto_project.dto.filters_dto.DataForProductsFiltersDto;
import planto_project.dto.NewProductDto;
import planto_project.dto.ProductDto;
import planto_project.dto.SortingDto;
import planto_project.service.ProductService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/product")
public class ProductController {
    final ProductService productService;

    @PostMapping("/create")
    public ProductDto createProduct(@RequestBody NewProductDto newProductDto) {
        return productService.createProduct(newProductDto);
    }

    @GetMapping("/quantity")
    public int getQuantity() {
        return  productService.getQuantity();
    }

    @PostMapping()
    public Page<ProductDto> findAllProducts(@RequestBody SortingDto sortingDto) {
        if (!sortingDto.getCriteria().isEmpty()) {
            return productService.findAllProductsWithCriteria(sortingDto);
        }
        return productService.findAllProducts(sortingDto);
    }

    @GetMapping("/{id}")
    public ProductDto getProduct(@PathVariable String id) {
        return productService.getProduct(id);
    }

    @PutMapping("/update/{id}")
    public ProductDto updateProduct(@PathVariable String id, @RequestBody ProductDto productDto) {
        return productService.updateProduct(id, productDto);
    }

    @PutMapping("/update/{id}/add-quantity/{amount}")
    public ProductDto AddQuantityOfProduct(@PathVariable String id, @PathVariable int amount) {
        return productService.changeQuantityOfProduct(id, amount, true);
    }

    @PutMapping("/update/{id}/reduce-quantity/{amount}")
    public ProductDto ReduceQuantityOfProduct(@PathVariable String id, @PathVariable int amount) {
        return productService.changeQuantityOfProduct(id, amount, false);
    }

    @DeleteMapping("/{id}")
    public ProductDto deleteProduct(@PathVariable String id) {
        return productService.deleteProduct(id);
    }

    @GetMapping("/filterdata")
    public DataForProductsFiltersDto getDataForFilters() {
        return productService.getDataForFilters();
    }
}


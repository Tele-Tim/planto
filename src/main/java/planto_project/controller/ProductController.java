package planto_project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import planto_project.dto.ProductDto;
import planto_project.service.ProductService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/product")
public class ProductController {
    final ProductService productService;

    @PostMapping("/create")
    public ProductDto createProduct(@RequestBody ProductDto productDto) {
        return productService.createProduct(productDto);
    }

    @GetMapping("/{id}")
    public ProductDto getProduct(@PathVariable String id) {
        return productService.getProduct(id);
    }

    @PutMapping("/{id}")
    public ProductDto updateProduct(@PathVariable String id, @RequestBody ProductDto productDto) {
        return productService.updateProduct(id, productDto);
    }
}

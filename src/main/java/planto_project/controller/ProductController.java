package planto_project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import planto_project.dto.NewProductDto;
import planto_project.dto.ProductDto;
import planto_project.service.ProductService;

import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/product")
//@CrossOrigin(origins = {"http://localhost:5173"}, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class ProductController {
    final ProductService productService;

    @PostMapping("/create")
    public ProductDto createProduct(@RequestBody NewProductDto newProductDto) {
        return productService.createProduct(newProductDto);
    }

    @GetMapping()
    public Set<ProductDto> findAllProducts() {
        System.out.println("All right");
        return productService.findAllProducts();
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



}

package planto_project.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import planto_project.dao.ProductRepository;
import planto_project.dto.NewProductDto;
import planto_project.dto.ProductDto;
import planto_project.model.Product;
import planto_project.validator.ProductValidator;

import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    final ProductRepository productRepository;
    final ModelMapper modelMapper;
    final ProductValidator productValidator;


    @Override
    public ProductDto createProduct(NewProductDto newProductDto) {
        Product product = modelMapper.map(newProductDto, Product.class);
        productRepository.save(product);
        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    public ProductDto getProduct(String id) {
        Product product = productRepository.findProductById(id);
        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    @Transactional
    public ProductDto updateProduct(String id, ProductDto productDto) {
        Product product = productRepository.findProductById(id);
        productValidator.updateFields(product, productDto);
        productRepository.save(product);
        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    @Transactional
    public ProductDto deleteProduct(String id) {
        Product product = productRepository.removeProductById(id);
        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    @Transactional
    public ProductDto changeQuantityOfProduct(String id, int amount, boolean flag) {
        Product product = productRepository.findProductById(id);
        product.changeQuantity(amount, flag);
        productRepository.save(product);
        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    public SortedSet<ProductDto> findAllProducts() {
        return productRepository.findAll(Sort.by("id")).stream()
                .map(p -> modelMapper.map(p, ProductDto.class))
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(ProductDto::getId))));
    }
}

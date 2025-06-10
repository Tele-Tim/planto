package planto_project.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import planto_project.dao.ProductRepository;
import planto_project.dto.ProductDto;
import planto_project.model.Product;
import planto_project.validator.ProductValidator;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    final ProductRepository productRepository;
    final ModelMapper modelMapper;
    final ProductValidator productValidator;


    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product product = modelMapper.map(productDto, Product.class);
        productRepository.save(product);
        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    public ProductDto getProduct(String id) {
        Product product = productRepository.findProductById(id);
        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    public ProductDto updateProduct(String id, ProductDto productDto) {
        Product product = productRepository.findProductById(id);
        productValidator.updateFields(product, productDto);
        productRepository.save(product);
        return modelMapper.map(product, ProductDto.class);
    }


}

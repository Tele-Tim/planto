package planto_project.dao;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import planto_project.dto.ProductDto;
import planto_project.model.Product;

import java.util.List;

@RequiredArgsConstructor
public class CustomProductRepositoryImpl implements CustomProductRepository {

    final MongoTemplate mongoTemplate;
    final ModelMapper modelMapper;

    @Override
    public Page<ProductDto> findProductsByQuery(Query query, Pageable pageable) {

        long totalCount = mongoTemplate.count(query, Product.class);

        Query pageQuery = Query.of(query).with(pageable);
        List<ProductDto> listProductDto = mongoTemplate.find(pageQuery, Product.class).stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .toList();

        return PageableExecutionUtils.getPage(listProductDto, pageable, () -> totalCount);
    }
}

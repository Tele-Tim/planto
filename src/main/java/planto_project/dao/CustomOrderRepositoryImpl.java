package planto_project.dao;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import planto_project.dto.OrderResponseDto;
import planto_project.dto.ProductDto;
import planto_project.model.Order;
import planto_project.model.Product;

import java.util.List;

@AllArgsConstructor
public class CustomOrderRepositoryImpl implements CustomOrderRepository{

    final MongoTemplate mongoTemplate;
    final ModelMapper modelMapper;

    @Override
    public Page<OrderResponseDto> findOrdersByQuery(Query query, Pageable pageable) {
        long totalCount = mongoTemplate.count(query, Order.class);

        Query pageQuery = Query.of(query).with(pageable);
        List<OrderResponseDto> listOrderDto = mongoTemplate.find(pageQuery, Order.class).stream()
                .map(product -> modelMapper.map(product, OrderResponseDto.class))
                .toList();

        return PageableExecutionUtils.getPage(listOrderDto, pageable, () -> totalCount);
    }
}

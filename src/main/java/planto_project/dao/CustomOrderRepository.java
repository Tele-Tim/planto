package planto_project.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import planto_project.dto.OrderResponseDto;

@Repository
public interface CustomOrderRepository{
    public Page<OrderResponseDto> findOrdersByQuery(Query query, Pageable pageable);
}

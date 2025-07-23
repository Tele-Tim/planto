package planto_project.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import planto_project.dto.ProductDto;

@Repository
public interface CustomProductRepository {
    Page<ProductDto> findProductsByQuery(Query query, Pageable pageable);
}

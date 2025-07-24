package planto_project.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.core.query.Query;
import planto_project.model.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String>, CustomProductRepository {
    Product findProductById(String id);

    Product removeProductById(String id);

    Page<Product> findProductByNameLikeIgnoreCase(String name, Pageable pageable);
    Page<Product> findProductsBy(Query query, Pageable pageable);
    Product getTop1ByOrderByPriceDesc();

    @Aggregation("{$group: {_id: $category}}")
    List<String> groupByCategory(Sort sort);
}
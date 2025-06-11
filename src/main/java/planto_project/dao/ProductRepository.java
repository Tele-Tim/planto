package planto_project.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import planto_project.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {
    Product findProductById(String id);

    Product removeProductById(String id);
}

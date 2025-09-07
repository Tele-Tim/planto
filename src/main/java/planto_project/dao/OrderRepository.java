package planto_project.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import planto_project.model.Order;

public interface OrderRepository extends MongoRepository<Order, String>, CustomOrderRepository {
}

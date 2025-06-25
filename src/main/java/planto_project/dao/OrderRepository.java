package planto_project.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import planto_project.model.Order;
import planto_project.model.UserAccount;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {

    List<Order> findAllByUser_Login(String userLogin);
}

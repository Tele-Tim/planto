package planto_project.service;

import org.springframework.stereotype.Service;
import planto_project.dto.OrderCreateDto;

@Service
public interface OrderService {
    OrderCreateDto createOrder(String userId, OrderCreateDto orderCreateDto);
}

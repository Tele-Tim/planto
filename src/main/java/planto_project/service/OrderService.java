package planto_project.service;

import org.springframework.stereotype.Service;
import planto_project.dto.OrderCreateDto;
import planto_project.dto.OrderResponseDto;

@Service
public interface OrderService {
    OrderCreateDto createOrder(String userId, OrderCreateDto orderCreateDto);

    OrderResponseDto findOrderById(String orderId);

    OrderResponseDto updateOrder(String orderId, OrderCreateDto orderCreateDto);

    OrderResponseDto removeOrder(String orderId);
}

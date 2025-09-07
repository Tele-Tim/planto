package planto_project.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import planto_project.dto.OrderCreateDto;
import planto_project.dto.OrderResponseDto;
import planto_project.dto.OrderUpdateDto;

import java.util.List;
import java.util.Set;
import planto_project.dto.SortingDto;

@Service
public interface OrderService {
    OrderCreateDto createOrder(String userId, OrderCreateDto orderCreateDto);

    OrderResponseDto findOrderById(String orderId);

    OrderResponseDto updateOrder(String orderId, OrderUpdateDto orderUpdateDto);

    OrderResponseDto removeOrder(String orderId);

    List<OrderResponseDto> findAllOrdersOfUser(String login);

    Set<OrderResponseDto> findAllOrders();

    Page<OrderResponseDto> findAllOrders(SortingDto sortingDto);

    Page<OrderResponseDto> findAllOrdersWithCriteria(SortingDto sortingDto);
}

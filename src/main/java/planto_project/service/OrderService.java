package planto_project.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import planto_project.dto.OrderCreateDto;
import planto_project.dto.OrderResponseDto;
import planto_project.dto.SortingDto;

@Service
public interface OrderService{
    OrderCreateDto createOrder(String userId, OrderCreateDto orderCreateDto);

    OrderResponseDto findOrderById(String orderId);

    OrderResponseDto updateOrder(String orderId, OrderCreateDto orderCreateDto);

    OrderResponseDto removeOrder(String orderId);

    Page<OrderResponseDto> finAllOrders(SortingDto sortingDto);

    Page<OrderResponseDto> findAllOrdersWithCriteria(SortingDto sortingDto);
}

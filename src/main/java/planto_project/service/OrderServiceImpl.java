package planto_project.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import planto_project.dao.AccountRepository;
import planto_project.dao.OrderRepository;
import planto_project.dto.OrderCreateDto;
import planto_project.dto.OrderResponseDto;
import planto_project.dto.SortingDto;
import planto_project.dto.filters_dto.DataForOrdersFiltersDto;
import planto_project.dto.filters_dto.FilterDto;
import planto_project.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService, DataServices, DataForFilters<DataForOrdersFiltersDto> {
    final AccountRepository accountRepository;
    final OrderRepository orderRepository;
    final ModelMapper modelMapper;

    @Override
    public OrderCreateDto createOrder(String userId, OrderCreateDto orderCreateDto) {
        UserAccount user = accountRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Order order = new Order(
                orderCreateDto.getItems().stream().map(i -> modelMapper.map(i, OrderItem.class)).toList(),
                LocalDateTime.now(),
                user.getAddress(),
                orderCreateDto.getPaymentMethod(),
                false);
        return modelMapper.map(orderRepository.save(order), OrderCreateDto.class);
    }

    @Override
    public OrderResponseDto findOrderById(String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        return modelMapper.map(order, OrderResponseDto.class);
    }

    @Override
    public OrderResponseDto updateOrder(String orderId, OrderCreateDto orderCreateDto) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        if (orderCreateDto.getShippingAddress() != null) {
            order.setShippingAddress(modelMapper.map(orderCreateDto.getShippingAddress(), Address.class));
        }
        order.setItems(orderCreateDto.getItems().stream()
                .map(i -> modelMapper.map(i, OrderItem.class)).toList());
        orderRepository.save(order);
        return modelMapper.map(order, OrderResponseDto.class);
    }

    @Override
    public OrderResponseDto removeOrder(String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        orderRepository.delete(order);
        return modelMapper.map(order, OrderResponseDto.class);
    }

    @Override
    public Page<OrderResponseDto> finAllOrders(SortingDto sortingDto) {
        return orderRepository.findAll(getPageable(sortingDto)).map(p -> modelMapper.map(p, OrderResponseDto.class));
    }

    @Override
    public Page<OrderResponseDto> findAllOrdersWithCriteria(SortingDto sortingDto) {

        Map<String, Filter<?>> filterMap = Map.ofEntries(
                Map.entry("status", new Filter<String>()),
                Map.entry("date", new Filter<LocalDate>()),
                Map.entry("sum", new Filter<Double>()),
                Map.entry("item", new Filter<String>())
        );

        Query query = getQueryWithCriteria(filterMap, sortingDto);

        return orderRepository.findOrdersByQuery(query, getPageable(sortingDto));
    }

    @Override
    public DataForOrdersFiltersDto getDataForFilters() {
        return new DataForOrdersFiltersDto();
    }
}

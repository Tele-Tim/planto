package planto_project.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import planto_project.dao.AccountRepository;
import planto_project.dao.OrderRepository;
import planto_project.dao.ProductRepository;
import planto_project.dto.*;
import planto_project.dto.filters_dto.DataForOrdersFiltersDto;
import planto_project.dto.filters_dto.DataForProductsFiltersDto;
import planto_project.model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService, DataServices, DataForFilters<DataForOrdersFiltersDto> {
    final AccountRepository accountRepository;
    final OrderRepository orderRepository;
    final ModelMapper modelMapper;
    final ProductRepository productRepository;

    @Override
    public OrderCreateDto createOrder(String userId, OrderCreateDto orderCreateDto) {
        UserAccount user = accountRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Order order = new Order(
                orderCreateDto.getItems().stream().map(i -> modelMapper.map(i, OrderItem.class)).toList(),
                LocalDateTime.now(),
                orderCreateDto.getPaymentMethod(),
                false,
                user);
        return modelMapper.map(orderRepository.save(order), OrderCreateDto.class);
    }

    @Override
    public OrderResponseDto findOrderById(String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        return modelMapper.map(order, OrderResponseDto.class);
    }

    @Override
    public OrderResponseDto updateOrder(String orderId, OrderUpdateDto orderUpdateDto) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setItems(orderUpdateDto.getItems().stream()
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
    public List<OrderResponseDto> findAllOrdersOfUser(String login) {
        UserAccount userAccount = accountRepository.findById(login)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<Order> orders = orderRepository.findAllByUser_Login(userAccount.getLogin());

        if (orders.isEmpty()) {
            return Collections.emptyList();
        }

        Set<String> productIds = orders.stream()
                .flatMap(o -> o.getItems().stream())
                .map(OrderItem::getProductId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        final Map<String, Product> productById = productRepository.findAllById(productIds).stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Product::getId, p -> p));

        return orders.stream()
                .map(order -> toOrderResponseDtoWithProductSnapshots(order, productById))
                .collect(Collectors.toList());
    }

    private OrderResponseDto toOrderResponseDtoWithProductSnapshots(Order order, Map<String, Product> productById) {
        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(order.getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus());
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setPaid(order.isPaid());

        List<OrderItemDto> items = order.getItems().stream()
                .map(item -> {
                    OrderItemDto itDto = new OrderItemDto();
                    itDto.setProductId(item.getProductId());
                    itDto.setQuantity(item.getQuantity());

                    Product product = productById.get(item.getProductId());
                    if (product != null) {
                        itDto.setName(product.getName());
                        itDto.setPriceUnit(product.getPrice() != null ? product.getPrice() : BigDecimal.ZERO);
                        itDto.setImageUrl(product.getImageUrl());
                    } else {
                        itDto.setName("Unknown product");
                        itDto.setPriceUnit(BigDecimal.ZERO);
                    }
                    return itDto;
                })
                .collect(Collectors.toList());
        dto.setItems(items);
        return dto;
    }



    @Override
    public Set<OrderResponseDto> findAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toSet());
    }

    private OrderResponseDto convertToDto(Order order) {
        OrderResponseDto dto = modelMapper.map(order, OrderResponseDto.class);
        dto.setItems(
                order.getItems().stream()
                        .map(this::convertToOrderItemDto)
                        .collect(Collectors.toList())
        );
        return dto;
    }

    private OrderItemDto convertToOrderItemDto(OrderItem orderItem) {
        OrderItemDto dto = modelMapper.map(orderItem, OrderItemDto.class);
        productRepository.findById(orderItem.getProductId()).ifPresent(product -> {
            dto.setName(product.getName());
            dto.setPriceUnit(product.getPrice());
        });
        return dto;
    }

    @Override
    public Page<OrderResponseDto> findAllOrders(SortingDto sortingDto) {
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

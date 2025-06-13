package planto_project.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import planto_project.dao.AccountRepository;
import planto_project.dao.OrderRepository;
import planto_project.dto.OrderCreateDto;
import planto_project.dto.OrderResponseDto;
import planto_project.model.Address;
import planto_project.model.Order;
import planto_project.model.OrderItem;
import planto_project.model.UserAccount;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
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
}

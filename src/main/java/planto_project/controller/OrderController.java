package planto_project.controller;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import planto_project.dto.OrderCreateDto;
import planto_project.dto.OrderResponseDto;
import planto_project.dto.SortingDto;
import planto_project.service.OrderService;

import java.util.Arrays;
import java.util.Comparator;

@RequiredArgsConstructor
@RestController
@RequestMapping("/order")
public class OrderController {
    final OrderService orderService;


    @PostMapping("/{login}")
    public OrderCreateDto createOrder(@PathVariable String login, @RequestBody OrderCreateDto orderCreateDto) {
        return orderService.createOrder(login, orderCreateDto);
    }

    @GetMapping("/{login}/{orderId}")
    public OrderResponseDto findOrder(@PathVariable String orderId) {
        return orderService.findOrderById(orderId);
    }

    @PutMapping("/{login}/{orderId}")
    public OrderResponseDto updateOrder(@PathVariable String orderId, @RequestBody OrderCreateDto orderCreateDto) {
        return orderService.updateOrder(orderId, orderCreateDto);
    }

    @DeleteMapping("/{login}/{orderId}")
    public OrderResponseDto removeOrder(@PathVariable String orderId) {
        return orderService.removeOrder(orderId);
    }

    @PostMapping("/all")
    public Page<OrderResponseDto> findAllOrders(@RequestBody SortingDto sortingDto) {
        if (sortingDto.getCriteria() != null
                && !sortingDto.getCriteria().isEmpty()) {
            return orderService.findAllOrdersWithCriteria(sortingDto);
        }
        return orderService.finAllOrders(sortingDto);
    }
}

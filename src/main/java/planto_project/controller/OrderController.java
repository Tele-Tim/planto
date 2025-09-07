package planto_project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.web.bind.annotation.*;
import planto_project.dto.OrderCreateDto;
import planto_project.dto.OrderResponseDto;
import planto_project.dto.SortingDto;
import planto_project.dto.OrderUpdateDto;
import planto_project.service.OrderService;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/order")
public class OrderController {
    final OrderService orderService;


    @PostMapping("create/{login}")
    public OrderCreateDto createOrder(@PathVariable String login, @RequestBody OrderCreateDto orderCreateDto) {
        return orderService.createOrder(login, orderCreateDto);
    }

    @GetMapping("/orders")
    public Set<OrderResponseDto> findAllOrders() {
        return orderService.findAllOrders();
    }

    @GetMapping("/{login}")
    public List<OrderResponseDto> findAllOrdersOfUser(@PathVariable String login) {
        return orderService.findAllOrdersOfUser(login);
    }


    @PutMapping("/{login}/{orderId}")
    public OrderResponseDto updateOrder(@PathVariable String orderId, @RequestBody OrderUpdateDto orderUpdateDto) {
        return orderService.updateOrder(orderId, orderUpdateDto);
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
        return orderService.findAllOrders(sortingDto);
    }
}

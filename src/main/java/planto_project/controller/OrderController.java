package planto_project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.web.bind.annotation.*;
import planto_project.dto.OrderCreateDto;
import planto_project.dto.OrderResponseDto;
import planto_project.service.OrderService;

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

}

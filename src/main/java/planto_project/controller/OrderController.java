package planto_project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import planto_project.dto.OrderCreateDto;
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

}

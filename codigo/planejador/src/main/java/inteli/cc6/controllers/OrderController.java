package inteli.cc6.controllers;

import inteli.cc6.dtos.OrderDto;
import inteli.cc6.dtos.OrderResponseDto;
import inteli.cc6.models.OrderModel;
import inteli.cc6.models.OrderSetModel;
import inteli.cc6.services.OrderService;
import inteli.cc6.services.OrderSetService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/order")
public class OrderController {

    final OrderService orderService;
    final OrderSetService orderSetService;

    public OrderController(OrderService orderService, OrderSetService orderSetService) {
        this.orderService = orderService;
        this.orderSetService = orderSetService;
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid OrderDto orderDto) {
        var orderModel = new OrderModel();

        BeanUtils.copyProperties(orderDto, orderModel);

        OrderSetModel orderSetModel = orderSetService.getById(orderDto.getOrderSetId());
        if(orderSetModel == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order Set not found");
        }

        orderModel.setOrderSet(orderSetModel);
        OrderModel savedOrderModel = orderService.save(orderModel);

        OrderResponseDto orderResponseDto = new OrderResponseDto();
        BeanUtils.copyProperties(savedOrderModel, orderResponseDto);
        orderResponseDto.setOrderSetId(savedOrderModel.getOrderSet().getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> read(@PathVariable UUID id) {
        OrderModel orderModel = orderService.getById(id);
        if(orderModel != null) {
            OrderResponseDto orderResponseDto = new OrderResponseDto();
            BeanUtils.copyProperties(orderModel, orderResponseDto);
            orderResponseDto.setOrderSetId(orderModel.getOrderSet().getId());
            return ResponseEntity.ok(orderResponseDto);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> readAll() {
        List<OrderModel> orderModels = orderService.getAll();
        List<OrderResponseDto> orderResponseDtos = new ArrayList<>();
        for (OrderModel orderModel : orderModels) {
            OrderResponseDto orderResponseDto = new OrderResponseDto();
            BeanUtils.copyProperties(orderModel, orderResponseDto);
            orderResponseDto.setOrderSetId(orderModel.getOrderSet().getId());
            orderResponseDtos.add(orderResponseDto);
        }
        return ResponseEntity.ok(orderResponseDtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable UUID id, @RequestBody @Valid OrderDto orderDto) {
        OrderModel existingOrderModel = orderService.getById(id);

        if (existingOrderModel != null) {
            BeanUtils.copyProperties(orderDto, existingOrderModel);

            OrderSetModel orderSetModel = orderSetService.getById(orderDto.getOrderSetId());
            if(orderSetModel == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order Set not found");
            }
            existingOrderModel.setOrderSet(orderSetModel);

            OrderModel updatedOrderModel = orderService.save(existingOrderModel);
            OrderResponseDto orderResponseDto = new OrderResponseDto();
            BeanUtils.copyProperties(updatedOrderModel, orderResponseDto);
            orderResponseDto.setOrderSetId(updatedOrderModel.getOrderSet().getId());

            return ResponseEntity.ok(orderResponseDto);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        orderService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
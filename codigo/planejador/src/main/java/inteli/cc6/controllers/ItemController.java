package inteli.cc6.controllers;

import inteli.cc6.dtos.ItemDto;
import inteli.cc6.dtos.ItemResponseDto;
import inteli.cc6.dtos.OrderDto;
import inteli.cc6.dtos.OrderResponseDto;
import inteli.cc6.models.ItemModel;
import inteli.cc6.models.OrderModel;
import inteli.cc6.models.OrderSetModel;
import inteli.cc6.services.ItemService;
import inteli.cc6.services.OrderService;
import inteli.cc6.services.OrderSetService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/item")
public class ItemController {

    final ItemService itemService;
    final OrderService orderService;
    final OrderSetService orderSetService;

    public ItemController(ItemService itemService, OrderService orderService, OrderSetService orderSetService) {
        this.itemService = itemService;
        this.orderService = orderService;
        this.orderSetService = orderSetService;
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid ItemDto itemDto) {
        var itemModel = new ItemModel();

        BeanUtils.copyProperties(itemDto, itemModel);

        OrderModel orderModel = orderService.getById(itemDto.getOrderId());
        if(orderModel == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
        }

        // Ensuring the OrderModel is associated with an OrderSetModel
        if (orderModel.getOrderSet() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Order is not associated with an OrderSet");
        }

        itemModel.setOrder(orderModel);
        ItemModel savedItemModel = itemService.save(itemModel);

        ItemResponseDto itemResponseDto = new ItemResponseDto();
        BeanUtils.copyProperties(savedItemModel, itemResponseDto);
        // Constructing OrderResponseDto to return with ItemResponseDto
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        BeanUtils.copyProperties(savedItemModel.getOrder(), orderResponseDto);
        orderResponseDto.setOrderSetId(savedItemModel.getOrder().getOrderSet().getId());
        itemResponseDto.setOrder(orderResponseDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(itemResponseDto);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDto> read(@PathVariable UUID id) {
        ItemModel itemModel = itemService.getById(id);
        if(itemModel != null) {
            ItemResponseDto itemResponseDto = new ItemResponseDto();
            BeanUtils.copyProperties(itemModel, itemResponseDto);

            OrderResponseDto orderResponseDto = new OrderResponseDto();
            BeanUtils.copyProperties(itemModel.getOrder(), orderResponseDto);

            // Manual copy of orderSetId
            if (itemModel.getOrder().getOrderSet() != null) {
                orderResponseDto.setOrderSetId(itemModel.getOrder().getOrderSet().getId());
            }

            itemResponseDto.setOrder(orderResponseDto);

            return ResponseEntity.ok(itemResponseDto);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    @GetMapping
    public ResponseEntity<List<ItemResponseDto>> readAll() {
        List<ItemResponseDto> itemResponseDtoList = itemService.getAll()
                .stream()
                .map(itemModel -> {
                    ItemResponseDto itemResponseDto = new ItemResponseDto();
                    BeanUtils.copyProperties(itemModel, itemResponseDto);

                    OrderResponseDto orderResponseDto = new OrderResponseDto();
                    BeanUtils.copyProperties(itemModel.getOrder(), orderResponseDto);

                    // Manual copy of orderSetId
                    if (itemModel.getOrder().getOrderSet() != null) {
                        orderResponseDto.setOrderSetId(itemModel.getOrder().getOrderSet().getId());
                    }

                    itemResponseDto.setOrder(orderResponseDto);

                    return itemResponseDto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(itemResponseDtoList);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable UUID id, @RequestBody @Valid ItemDto itemDto) {
        ItemModel existingItemModel = itemService.getById(id);

        if (existingItemModel != null) {
            // Update only width and amount properties
            existingItemModel.setWidth(itemDto.getWidth());
            existingItemModel.setAmount(itemDto.getAmount());

            // Update the OrderModel only if orderId from ItemDto is different from existing one
            if (!existingItemModel.getOrder().getId().equals(itemDto.getOrderId())) {
                OrderModel newOrderModel = orderService.getById(itemDto.getOrderId());
                if (newOrderModel == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order with id " + itemDto.getOrderId() + " not found");
                }
                existingItemModel.setOrder(newOrderModel);
            }

            ItemModel updatedItemModel = itemService.save(existingItemModel);

            ItemResponseDto itemResponseDto = new ItemResponseDto();
            BeanUtils.copyProperties(updatedItemModel, itemResponseDto);
            OrderResponseDto orderResponseDto = new OrderResponseDto();
            BeanUtils.copyProperties(updatedItemModel.getOrder(), orderResponseDto);
            // Add orderSetId to the response
            if(updatedItemModel.getOrder().getOrderSet() != null) {
                orderResponseDto.setOrderSetId(updatedItemModel.getOrder().getOrderSet().getId());
            }
            itemResponseDto.setOrder(orderResponseDto);

            return ResponseEntity.ok(itemResponseDto);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        itemService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

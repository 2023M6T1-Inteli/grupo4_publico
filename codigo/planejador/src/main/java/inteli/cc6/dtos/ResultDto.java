package inteli.cc6.dtos;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ResultDto {
    List<OrderDto> orders;
    List<ItemDto> items;

    public ResultDto(List<OrderDto> orders, List<ItemDto> items) {
        this.orders = orders;
        this.items = items;
    }

    public List<OrderDto> getOrders() {
        return orders;
    }

    public List<ItemDto> getItems() {
        return items;
    }

    public List<ItemDto> getItems(String salesOrderId) {
        return items.stream().filter(item -> item.getSalesOrderId().equals(salesOrderId)).collect(Collectors.toList());
    }
}
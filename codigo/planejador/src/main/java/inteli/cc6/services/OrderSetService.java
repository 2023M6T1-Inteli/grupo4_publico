package inteli.cc6.services;

import inteli.cc6.dtos.ItemDto;
import inteli.cc6.dtos.OrderDto;
import inteli.cc6.models.ItemModel;
import inteli.cc6.models.MachineModel;
import inteli.cc6.models.OrderModel;
import inteli.cc6.models.OrderSetModel;
import inteli.cc6.repositories.ItemRepository;
import inteli.cc6.repositories.OrderRepository;
import inteli.cc6.repositories.OrderSetRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderSetService {

    final OrderSetRepository orderSetRepository;
    final OrderRepository orderRepository;
    final ItemRepository itemRepository;

    public OrderSetService(OrderSetRepository orderSetRepository, OrderRepository orderRepository, ItemRepository itemRepository) {
        this.orderSetRepository = orderSetRepository;
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional
    public OrderSetModel save(OrderSetModel orderSetModel) {
        return orderSetRepository.save(orderSetModel);
    }

    @Transactional
    public OrderSetModel getById(UUID id) {
        Optional<OrderSetModel> orderSetModel = orderSetRepository.findById(id);
        if(orderSetModel.isPresent()) {
            return orderSetModel.get();
        }
        else {
            return null; // Throw an exception or return a suitable default here
        }
    }

    @Transactional
    public List<OrderSetModel> getAll() {
        return orderSetRepository.findAll();
    }

    @Transactional
    public void deleteById(UUID id) {
        orderSetRepository.deleteById(id);
    }

    @Transactional
    public void handleFileUpload(String fileName, byte[] contents) {
        try {
            Path path = Paths.get("./uploads/" + fileName);

            // Ensure the directory exists
            Files.createDirectories(path.getParent());

            // Write the file's contents to the path
            Files.write(path, contents);

            System.out.println("File saved successfully");
        } catch (IOException e) {
            System.err.println("Error saving file: " + e.getMessage());
        }
    }

    @Transactional
    public byte[] getFileBytes(String fileName) throws IOException {
        Path path = Paths.get("./uploads/" + fileName);
        return Files.readAllBytes(path);
    }

    @Transactional
    public void saveOrders(List<OrderDto> orderDtos, OrderSetModel orderSet) {
        List<OrderModel> orders = new ArrayList<>();
        for (OrderDto orderDto : orderDtos) {
            OrderModel orderModel = new OrderModel();
            // Here, we're copying properties from orderDto to orderModel
            BeanUtils.copyProperties(orderDto, orderModel);
            orderModel.setOrderSet(orderSet);
            orders.add(orderModel);
        }
        orderSet.setOrders(orders);
        orderSetRepository.save(orderSet);
    }

    @Transactional
    public void saveItems(List<ItemDto> itemDtos, OrderModel order) {
        List<ItemModel> items = new ArrayList<>();
        for (ItemDto itemDto : itemDtos) {
            ItemModel itemModel = new ItemModel();
            // Here, we're copying properties from itemDto to itemModel
            BeanUtils.copyProperties(itemDto, itemModel);
            itemModel.setOrder(order);
            items.add(itemModel);
        }
        order.setItems(items);
        orderRepository.save(order);
    }


}

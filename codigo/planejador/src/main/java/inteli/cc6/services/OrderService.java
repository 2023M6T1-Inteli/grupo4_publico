package inteli.cc6.services;

import inteli.cc6.models.OrderModel;
import inteli.cc6.repositories.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderModel save(OrderModel orderModel) {
        return orderRepository.save(orderModel);
    }

    @Transactional
    public OrderModel getById(UUID id) {
        Optional<OrderModel> orderModel = orderRepository.findById(id);
        if(orderModel.isPresent()) {
            return orderModel.get();
        }
        else {
            return null; // Throw an exception or return a suitable default here
        }
    }

    @Transactional
    public List<OrderModel> getAll() {
        return orderRepository.findAll();
    }

    @Transactional
    public void deleteById(UUID id) {
        orderRepository.deleteById(id);
    }
}

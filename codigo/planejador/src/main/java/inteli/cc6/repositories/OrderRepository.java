package inteli.cc6.repositories;


import inteli.cc6.models.OrderModel;
import inteli.cc6.models.OrderSetModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<OrderModel, UUID> {

}

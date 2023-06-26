package inteli.cc6.repositories;

import inteli.cc6.models.MachineModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MachineRepository extends JpaRepository<MachineModel, UUID> {

}

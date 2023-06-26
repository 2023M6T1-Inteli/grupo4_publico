package inteli.cc6.repositories;


import inteli.cc6.models.CalcModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CalcRepository extends JpaRepository<CalcModel, UUID>{
}

package inteli.cc6.repositories;

import inteli.cc6.models.CalcModel;
import inteli.cc6.models.CuttingPatternModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CuttingPatternRepository extends JpaRepository<CuttingPatternModel, UUID>{
    List<CuttingPatternModel> findByCalc(CalcModel calcModel);
}

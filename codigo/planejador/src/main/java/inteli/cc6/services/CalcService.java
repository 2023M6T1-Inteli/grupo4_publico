package inteli.cc6.services;

import inteli.cc6.models.CalcModel;
import inteli.cc6.repositories.CalcRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CalcService {

    final CalcRepository calcRepository;

    public CalcService(CalcRepository calcRepository) {
        this.calcRepository = calcRepository;
    }

    @Transactional
    public CalcModel save(CalcModel calcModel) {
        return calcRepository.save(calcModel);
    }

    @Transactional
    public CalcModel getById(UUID id) {
        Optional<CalcModel> calcModel = calcRepository.findById(id);
        if(calcModel.isPresent()) {
            return calcModel.get();
        }
        else {
            return null; // Throw an exception or return a suitable default here
        }
    }

    @Transactional
    public List<CalcModel> getAll() {
        return calcRepository.findAll();
    }

    @Transactional
    public void deleteById(UUID id) {
        calcRepository.deleteById(id);
    }

}

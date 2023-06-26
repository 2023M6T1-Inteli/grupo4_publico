package inteli.cc6.services;

import inteli.cc6.models.CuttingPatternModel;
import inteli.cc6.repositories.CuttingPatternRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CuttingPatternService {

    final CuttingPatternRepository cuttingPatternRepository;

    public CuttingPatternService(CuttingPatternRepository cuttingPatternRepository) {
        this.cuttingPatternRepository = cuttingPatternRepository;
    }

    @Transactional
    public CuttingPatternModel save(CuttingPatternModel cuttingPatternModel) {
        return cuttingPatternRepository.save(cuttingPatternModel);
    }

    @Transactional
    public CuttingPatternModel getById(UUID id) {
        Optional<CuttingPatternModel> cuttingPatternModel = cuttingPatternRepository.findById(id);
        if(cuttingPatternModel.isPresent()) {
            return cuttingPatternModel.get();
        }
        else {
            return null; // Throw an exception or return a suitable default here
        }
    }

    @Transactional
    public List<CuttingPatternModel> getAll() {
        return cuttingPatternRepository.findAll();
    }

    @Transactional
    public void deleteById(UUID id) {
        cuttingPatternRepository.deleteById(id);
    }

    public CuttingPatternRepository getCuttingPatternRepository() {
        return cuttingPatternRepository;
    }

}

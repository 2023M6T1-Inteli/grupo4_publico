package inteli.cc6.services;

import inteli.cc6.models.MachineModel;
import inteli.cc6.repositories.MachineRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MachineService {

    final MachineRepository machineRepository;

    public MachineService(MachineRepository machineRepository) {
        this.machineRepository = machineRepository;
    }

    @Transactional
    public MachineModel save(MachineModel machineModel) {
        return machineRepository.save(machineModel);
    }

    @Transactional
    public MachineModel getById(UUID id) {
        Optional<MachineModel> machineModel = machineRepository.findById(id);
        if(machineModel.isPresent()) {
            return machineModel.get();
        }
        else {
            return null; // Throw an exception or return a suitable default here
        }
    }

    @Transactional
    public List<MachineModel> getAll() {
        return machineRepository.findAll();
    }

    @Transactional
    public void deleteById(UUID id) {
        machineRepository.deleteById(id);
    }
}

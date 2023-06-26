package inteli.cc6.controllers;

import inteli.cc6.dtos.MachineDto;
import inteli.cc6.models.MachineModel;
import inteli.cc6.services.MachineService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/machine")
public class MachineController {

    final MachineService machineService;

    public MachineController(MachineService machineService) {
        this.machineService = machineService;
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid MachineDto machineDto) {
        var machineModel = new MachineModel();

        BeanUtils.copyProperties(machineDto, machineModel);
        machineModel.setCreatedAt(LocalDateTime.now(ZoneId.of("UTC")));

        return ResponseEntity.status(HttpStatus.CREATED).body(
                machineService.save(machineModel)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<MachineModel> read(@PathVariable UUID id) {
        return ResponseEntity.ok(machineService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<MachineModel>> readAll() {
        return ResponseEntity.ok(machineService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable UUID id, @RequestBody @Valid MachineDto machineDto) {
        MachineModel existingMachineModel = machineService.getById(id);

        if (existingMachineModel != null) {
            BeanUtils.copyProperties(machineDto, existingMachineModel, "createdAt");
            return ResponseEntity.ok(machineService.save(existingMachineModel));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        machineService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

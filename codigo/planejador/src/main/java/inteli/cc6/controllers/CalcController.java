package inteli.cc6.controllers;

import inteli.cc6.algorithm.BranchAndPrice;
import inteli.cc6.dtos.CuttingPatternShortResponseDto;
import inteli.cc6.models.CalcModel;
import inteli.cc6.models.OrderSetModel;
import inteli.cc6.models.MachineModel;
import inteli.cc6.dtos.CalcDto;
import inteli.cc6.dtos.CalcResponseDto;
import inteli.cc6.services.CalcService;
import inteli.cc6.services.OrderSetService;
import inteli.cc6.services.MachineService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import inteli.cc6.services.CuttingPatternService;
import inteli.cc6.dtos.CuttingPatternResponseDto;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/calc")
public class CalcController {

    private final CalcService calcService;
    private final OrderSetService orderSetService;
    private final MachineService machineService;
    private final CuttingPatternService cuttingPatternService;

    public CalcController(CalcService calcService, OrderSetService orderSetService, MachineService machineService, CuttingPatternService cuttingPatternService) {
        this.calcService = calcService;
        this.orderSetService = orderSetService;
        this.machineService = machineService;
        this.cuttingPatternService = cuttingPatternService;
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid CalcDto calcDto) {
        var calcModel = new CalcModel();
        BeanUtils.copyProperties(calcDto, calcModel);

        OrderSetModel orderSetModel = orderSetService.getById(calcDto.getOrderSetId());
        if(orderSetModel == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order Set not found");
        }
        calcModel.setOrderSet(orderSetModel);

        MachineModel machineModel = machineService.getById(calcDto.getMachineId());
        if(machineModel == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Machine not found");
        }
        calcModel.setMachine(machineModel);

        calcModel = calcService.save(calcModel);

        BranchAndPrice branchAndPrice = new BranchAndPrice(calcService, calcModel, cuttingPatternService.getCuttingPatternRepository(), orderSetModel, machineModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(calcModel));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CalcResponseDto> read(@PathVariable UUID id) {
        CalcModel calcModel = calcService.getById(id);
        if(calcModel != null) {
            return ResponseEntity.ok(toDto(calcModel));
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<CalcResponseDto>> readAll() {
        List<CalcResponseDto> response = calcService.getAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable UUID id, @RequestBody @Valid CalcDto calcDto) {
        CalcModel existingCalcModel = calcService.getById(id);

        if (existingCalcModel != null) {
            BeanUtils.copyProperties(calcDto, existingCalcModel);
            existingCalcModel.setId(id); // Ensure ID remains the same
            return ResponseEntity.ok(toDto(calcService.save(existingCalcModel)));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        calcService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private CalcResponseDto toDto(CalcModel model) {
        CalcResponseDto dto = new CalcResponseDto();
        BeanUtils.copyProperties(model, dto);

        if(model.getOrderSet() != null) {
            dto.setOrderSetId(model.getOrderSet().getId());
        }

        if(model.getMachine() != null) {
            dto.setMachineId(model.getMachine().getId());

            // NEW: Add machine data to DTO
            dto.setMachineName(model.getMachine().getName());
            dto.setMachineMinWidth(model.getMachine().getMinWidth());
            dto.setMachineMaxWidth(model.getMachine().getMaxWidth());
            dto.setMachineKnives(model.getMachine().getKnives());
            dto.setMachineCreatedAt(model.getMachine().getCreatedAt());
        }

        // Convert CuttingPatternModel to CuttingPatternShortResponseDto
        if(model.getCuttingPatterns() != null) {
            List<CuttingPatternShortResponseDto> cuttingPatternShortResponseDtoList = model.getCuttingPatterns().stream()
                    .map(cuttingPatternModel -> {
                        CuttingPatternShortResponseDto cuttingPatternShortResponseDto = new CuttingPatternShortResponseDto();
                        BeanUtils.copyProperties(cuttingPatternModel, cuttingPatternShortResponseDto);
                        return cuttingPatternShortResponseDto;
                    }).collect(Collectors.toList());

            dto.setCuttingPatterns(cuttingPatternShortResponseDtoList);
        }

        return dto;
    }


}

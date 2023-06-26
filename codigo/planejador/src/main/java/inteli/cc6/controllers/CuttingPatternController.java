package inteli.cc6.controllers;

import inteli.cc6.dtos.CuttingPatternDto;
import inteli.cc6.dtos.CuttingPatternResponseDto;
import inteli.cc6.dtos.CalcShortResponseDto;
import inteli.cc6.models.CuttingPatternModel;
import inteli.cc6.models.CalcModel;
import inteli.cc6.services.CuttingPatternService;
import inteli.cc6.services.CalcService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/cutting-pattern")
public class CuttingPatternController {

    final CuttingPatternService cuttingPatternService;
    final CalcService calcService;

    public CuttingPatternController(CuttingPatternService cuttingPatternService, CalcService calcService) {
        this.cuttingPatternService = cuttingPatternService;
        this.calcService = calcService;
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid CuttingPatternDto cuttingPatternDto) {
        var cuttingPatternModel = new CuttingPatternModel();
        BeanUtils.copyProperties(cuttingPatternDto, cuttingPatternModel);

        CalcModel calcModel = calcService.getById(cuttingPatternDto.getCalcId());
        if(calcModel == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Calculation not found");
        }

        cuttingPatternModel.setCalc(calcModel);
        CuttingPatternModel savedCuttingPatternModel = cuttingPatternService.save(cuttingPatternModel);

        CuttingPatternResponseDto cuttingPatternResponseDto = new CuttingPatternResponseDto();
        BeanUtils.copyProperties(savedCuttingPatternModel, cuttingPatternResponseDto);

        CalcShortResponseDto calcShortResponseDto = new CalcShortResponseDto();
        BeanUtils.copyProperties(savedCuttingPatternModel.getCalc(), calcShortResponseDto);
        cuttingPatternResponseDto.setCalc(calcShortResponseDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(cuttingPatternResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuttingPatternResponseDto> read(@PathVariable UUID id) {
        CuttingPatternModel cuttingPatternModel = cuttingPatternService.getById(id);
        if(cuttingPatternModel != null) {
            CuttingPatternResponseDto cuttingPatternResponseDto = new CuttingPatternResponseDto();
            BeanUtils.copyProperties(cuttingPatternModel, cuttingPatternResponseDto);

            CalcShortResponseDto calcShortResponseDto = new CalcShortResponseDto();
            BeanUtils.copyProperties(cuttingPatternModel.getCalc(), calcShortResponseDto);
            cuttingPatternResponseDto.setCalc(calcShortResponseDto);

            return ResponseEntity.ok(cuttingPatternResponseDto);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping
    public ResponseEntity<List<CuttingPatternResponseDto>> readAll() {
        List<CuttingPatternResponseDto> cuttingPatternResponseDtoList = cuttingPatternService.getAll()
                .stream()
                .map(cuttingPatternModel -> {
                    CuttingPatternResponseDto cuttingPatternResponseDto = new CuttingPatternResponseDto();
                    BeanUtils.copyProperties(cuttingPatternModel, cuttingPatternResponseDto);

                    CalcShortResponseDto calcShortResponseDto = new CalcShortResponseDto();
                    BeanUtils.copyProperties(cuttingPatternModel.getCalc(), calcShortResponseDto);
                    cuttingPatternResponseDto.setCalc(calcShortResponseDto);

                    return cuttingPatternResponseDto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(cuttingPatternResponseDtoList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable UUID id, @RequestBody @Valid CuttingPatternDto cuttingPatternDto) {
        CuttingPatternModel existingCuttingPatternModel = cuttingPatternService.getById(id);

        if (existingCuttingPatternModel != null) {
            BeanUtils.copyProperties(cuttingPatternDto, existingCuttingPatternModel);
            existingCuttingPatternModel.setCalc(calcService.getById(cuttingPatternDto.getCalcId()));

            CuttingPatternModel updatedCuttingPatternModel = cuttingPatternService.save(existingCuttingPatternModel);

            CuttingPatternResponseDto cuttingPatternResponseDto = new CuttingPatternResponseDto();
            BeanUtils.copyProperties(updatedCuttingPatternModel, cuttingPatternResponseDto);

            CalcShortResponseDto calcShortResponseDto = new CalcShortResponseDto();
            BeanUtils.copyProperties(updatedCuttingPatternModel.getCalc(), calcShortResponseDto);
            cuttingPatternResponseDto.setCalc(calcShortResponseDto);

            return ResponseEntity.ok(cuttingPatternResponseDto);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        cuttingPatternService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

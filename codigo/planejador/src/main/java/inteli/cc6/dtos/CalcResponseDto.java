package inteli.cc6.dtos;

import inteli.cc6.models.CalcModel;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CalcResponseDto {

    private UUID id;
    private String name;
    private UUID orderSetId;
    private UUID machineId;
    private String width;
    private List<CuttingPatternShortResponseDto> cuttingPatterns;

    private String machineName;
    private int machineMinWidth;
    private int machineMaxWidth;
    private int machineKnives;
    private LocalDateTime machineCreatedAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getOrderSetId() {
        return orderSetId;
    }

    public void setOrderSetId(UUID orderSetId) {
        this.orderSetId = orderSetId;
    }

    public UUID getMachineId() {
        return machineId;
    }

    public void setMachineId(UUID machineId) {
        this.machineId = machineId;
    }

    public List<CuttingPatternShortResponseDto> getCuttingPatterns() {
        return cuttingPatterns;
    }
    
    public void setCuttingPatterns(List<CuttingPatternShortResponseDto> cuttingPatternShortResponseDtoList) {
        this.cuttingPatterns = cuttingPatternShortResponseDtoList;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public int getMachineMinWidth() {
        return machineMinWidth;
    }

    public void setMachineMinWidth(int machineMinWidth) {
        this.machineMinWidth = machineMinWidth;
    }

    public int getMachineMaxWidth() {
        return machineMaxWidth;
    }

    public void setMachineMaxWidth(int machineMaxWidth) {
        this.machineMaxWidth = machineMaxWidth;
    }

    public int getMachineKnives() {
        return machineKnives;
    }

    public void setMachineKnives(int machineKnives) {
        this.machineKnives = machineKnives;
    }

    public LocalDateTime getMachineCreatedAt() {
        return machineCreatedAt;
    }

    public void setMachineCreatedAt(LocalDateTime machineCreatedAt) {
        this.machineCreatedAt = machineCreatedAt;
    }
}

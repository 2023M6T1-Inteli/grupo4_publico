package inteli.cc6.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public class CalcDto {

    @NotBlank
    private String name;

    @NotNull
    private UUID orderSetId;

    @NotNull
    private UUID machineId;
    private List<UUID> cuttingPatternIds;

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

    public List<UUID> getCuttingPatternIds() {
        return cuttingPatternIds;
    }

    public void setCuttingPatternIds(List<UUID> cuttingPatternIds) {
        this.cuttingPatternIds = cuttingPatternIds;
    }

}

package inteli.cc6.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class CuttingPatternDto {

    @NotNull
    private int drawn;

    @NotBlank
    private String pattern;

    private UUID calcId;

    public int getDrawn() {
        return drawn;
    }

    public void setDrawn(int drawn) {
        this.drawn = drawn;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public UUID getCalcId() {
        return calcId;
    }

    public void setCalcId(UUID calcId) {
        this.calcId = calcId;
    }

}

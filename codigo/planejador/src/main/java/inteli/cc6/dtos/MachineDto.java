package inteli.cc6.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class MachineDto {

    @NotBlank
    private String name;

    @NotNull
    @Positive
    private int minWidth;

    @NotNull
    @Positive
    private int maxWidth;

    @NotNull
    @Positive
    private int knives;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(int minWidth) {
        this.minWidth = minWidth;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    public int getKnives() {
        return knives;
    }

    public void setKnives(int knives) {
        this.knives = knives;
    }

}

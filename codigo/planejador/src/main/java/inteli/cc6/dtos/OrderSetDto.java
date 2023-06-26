package inteli.cc6.dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;

public class OrderSetDto {

    @NotBlank
    private String name;

    @NotBlank
    private String fileName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}

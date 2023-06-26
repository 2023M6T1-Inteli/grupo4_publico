package inteli.cc6.dtos;

import java.util.UUID;

public class CuttingPatternShortResponseDto {
    private UUID id;
    private Integer drawn;
    private String pattern;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getDrawn() {
        return drawn;
    }

    public void setDrawn(Integer drawn) {
        this.drawn = drawn;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

}

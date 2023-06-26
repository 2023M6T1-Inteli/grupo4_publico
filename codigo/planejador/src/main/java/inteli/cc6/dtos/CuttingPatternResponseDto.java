package inteli.cc6.dtos;

import java.util.UUID;

public class CuttingPatternResponseDto {

    private UUID id;
    private int drawn;
    private String pattern;
    private CalcShortResponseDto calc;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public CalcShortResponseDto getCalc() {
        return calc;
    }

    public void setCalc(CalcShortResponseDto calc) {
        this.calc = calc;
    }

}

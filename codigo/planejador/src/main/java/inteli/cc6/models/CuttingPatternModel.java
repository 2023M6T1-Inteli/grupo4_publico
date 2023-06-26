package inteli.cc6.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name="tb_cutting_pattern")
public class CuttingPatternModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private int drawn;

    @Column(nullable = false)
    private String pattern;

    @ManyToOne
    @JoinColumn(name = "calc_id")
    @JsonBackReference
    private CalcModel calc;

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

    public CalcModel getCalc() {
        return calc;
    }

    public void setCalc(CalcModel calc) {
        this.calc = calc;
    }

}

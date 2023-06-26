package inteli.cc6.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="tb_calc")
public class CalcModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column
    private String width;

    @ManyToOne
    @JoinColumn(name = "order_set_id")
    @JsonBackReference
    private OrderSetModel orderSet;

    @ManyToOne
    @JoinColumn(name = "machine_id")
    @JsonBackReference
    private MachineModel machine;

    @OneToMany(mappedBy = "calc", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<CuttingPatternModel> cuttingPatterns;

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

    public OrderSetModel getOrderSet() {
        return orderSet;
    }

    public void setOrderSet(OrderSetModel orderSet) {
        this.orderSet = orderSet;
    }

    public MachineModel getMachine() {
        return machine;
    }

    public void setMachine(MachineModel machine) {
        this.machine = machine;
    }

    public List<CuttingPatternModel> getCuttingPatterns() {
        return cuttingPatterns;
    }

    public void setCuttingPatterns(List<CuttingPatternModel> cuttingPatterns) {
        this.cuttingPatterns = cuttingPatterns;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

}

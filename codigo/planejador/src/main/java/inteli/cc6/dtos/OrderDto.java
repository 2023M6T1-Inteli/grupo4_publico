package inteli.cc6.dtos;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public class OrderDto {

    @NotBlank
    private String salesOrderId;

    private UUID orderSetId;

    public String getSalesOrderId() {
        return salesOrderId;
    }

    public void setSalesOrderId(String salesOrderId) {
        this.salesOrderId = salesOrderId;
    }

    public UUID getOrderSetId() {
        return orderSetId;
    }

    public void setOrderSetId(UUID orderSetId) {
        this.orderSetId = orderSetId;
    }

}

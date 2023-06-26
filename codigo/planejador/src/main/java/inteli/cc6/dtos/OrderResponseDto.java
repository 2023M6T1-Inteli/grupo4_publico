package inteli.cc6.dtos;

import java.util.UUID;

public class OrderResponseDto {

    private UUID id;
    private String salesOrderId;
    private UUID orderSetId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

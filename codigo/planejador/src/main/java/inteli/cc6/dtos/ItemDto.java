package inteli.cc6.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class ItemDto {

    @NotNull
    private int width; // width property

    @NotNull
    private int amount; // amount property

    private UUID orderId;

    private String salesOrderId;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public String getSalesOrderId() {
        return salesOrderId;
    }

    public void setSalesOrderId(String salesOrderId) {
        this.salesOrderId = salesOrderId;
    }

}

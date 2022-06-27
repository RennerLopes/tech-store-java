package br.edu.femass.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SaleDetail {
    private Integer quantity;
    private BigDecimal price;
    private Sale sale;
    private Product product;

    @Override
    public String toString() {
        return this.product + " - " + this.quantity + " UN | R$" + this.price;
    }
}

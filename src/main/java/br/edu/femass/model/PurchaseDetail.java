package br.edu.femass.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PurchaseDetail {
    private Integer quantity;
    private BigDecimal price;
    private Purchase purchase;
    private Product product;

    @Override
    public String toString() {
        return this.product + " - " + this.quantity + " UN | R$" + this.price;
    }
}

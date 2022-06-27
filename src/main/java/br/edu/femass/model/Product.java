package br.edu.femass.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Product {
    private Long id;
    private String name;
    private BigDecimal PurchasePrice;
    private Integer stock;
    private Category category;
    private BigDecimal SalePrice;

    @Override
    public String toString() {
        return this.name;
    }
}


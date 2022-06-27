package br.edu.femass.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class Sale {
    private Long id;
    private Date date;
    private BigDecimal total;
    private Client client;
    private List<SaleDetail> saleDetailList;

    @Override
    public String toString() {
        return this.client.getName() + " | " + this.date + " | R$ " + this.total.toString();
    }
}

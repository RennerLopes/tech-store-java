package br.edu.femass.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class Purchase {
    private Long id;
    private Date date;
    private BigDecimal total;
    private Supplier supplier;
    private List<PurchaseDetail> purchaseDetailList;

    @Override
    public String toString() {
        return this.supplier.getName() + " | " + this.date;
    }
}

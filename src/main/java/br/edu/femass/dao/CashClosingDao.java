package br.edu.femass.dao;

import br.edu.femass.model.Client;
import br.edu.femass.model.Purchase;
import br.edu.femass.model.Sale;
import br.edu.femass.model.Supplier;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CashClosingDao extends DaoPostgres{
    public List<Purchase> retrievePurchasesByDay() throws Exception {
        String sql = "SELECT " +
                "purchase.id, " +
                "purchase.total, " +
                "purchase.date, " +
                "supplier.id as supplier_id, " +
                "supplier.name " +
                "FROM Purchase, supplier " +
                "WHERE purchase.id_supplier = supplier.id AND " +
                "date = CURRENT_DATE " +
                "ORDER BY purchase.id";
        PreparedStatement ps = getPreparedStatment(sql, false);
        ResultSet rs = ps.executeQuery();

        List<Purchase> purchases = new ArrayList<Purchase>();
        while(rs.next()) {
            Purchase purchase = new Purchase();
            purchase.setTotal(rs.getBigDecimal("total"));
            purchase.setDate(rs.getDate("date"));
            purchase.setId(rs.getLong("id"));

            Supplier supplier = new Supplier();
            supplier.setName(rs.getString("name"));
            supplier.setId(rs.getLong("supplier_id"));
            purchase.setSupplier(supplier);

            purchases.add(purchase);
        }

        return purchases;
    }

    public List<Sale> retrieveSalesByDay() throws Exception {
            String sql = "SELECT " +
                    "sale.id, " +
                    "sale.total, " +
                    "sale.date, " +
                    "client.id as client_id, " +
                    "client.name " +
                    "FROM sale, client " +
                    "WHERE sale.id_client = client.id AND " +
                    "date = CURRENT_DATE " +
                    "ORDER BY client.id";
            PreparedStatement ps = getPreparedStatment(sql, false);
            ResultSet rs = ps.executeQuery();

            List<Sale> sales = new ArrayList<Sale>();
            while(rs.next()) {
                Sale sale = new Sale();
                sale.setTotal(rs.getBigDecimal("total"));
                sale.setDate(rs.getDate("date"));
                sale.setId(rs.getLong("id"));

                Client client = new Client();
                client.setName(rs.getString("name"));
                client.setId(rs.getLong("client_id"));
                sale.setClient(client);

                sales.add(sale);
            }

            return sales;
    }
}

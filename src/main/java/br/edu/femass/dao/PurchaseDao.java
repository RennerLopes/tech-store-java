package br.edu.femass.dao;

import br.edu.femass.model.*;
import br.edu.femass.model.Purchase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PurchaseDao extends DaoPostgres implements Dao<Purchase>{
    @Override
    public List<Purchase> retrieve() throws Exception {
        String sql = "SELECT " +
                "purchase.id as purchase_id, " +
                "purchase.date as purchase_date, " +
                "purchase.total as purchase_total, " +
                "supplier.id as supplier_id, " +
                "supplier.name as supplier_name, " +
                "supplier.cnpj as supplier_cnpj, " +
                "supplier.phone as supplier_phone, " +
                "supplier.address as supplier_address " +
                "FROM purchase " +
                "INNER JOIN supplier ON supplier.id = purchase.id_supplier";
        PreparedStatement ps = getPreparedStatment(sql, false);
        ResultSet rs = ps.executeQuery();

        List<Purchase> purchaseList = new ArrayList<Purchase>();
        while(rs.next()) {
            Purchase purchase = new Purchase();
            purchase.setId(rs.getLong("purchase_id"));
            purchase.setDate(rs.getDate("purchase_date"));
            purchase.setTotal(rs.getBigDecimal("purchase_total"));

            Supplier supplier = new Supplier();
            supplier.setId(rs.getLong("supplier_id"));
            supplier.setAddress(rs.getString("supplier_address"));
            supplier.setPhone(rs.getString("supplier_phone"));
            supplier.setName(rs.getString("supplier_name"));
            supplier.setCnpj(rs.getString("supplier_cnpj"));
            purchase.setSupplier(supplier);

            String sql2 = "SELECT " +
                    "purchaseDetail.quantity, " +
                    "purchaseDetail.price, " +
                    "product.id, " +
                    "product.name " +
                    "FROM purchaseDetail, product " +
                    "WHERE purchaseDetail.id_purchase = ? AND " +
                    "purchaseDetail.id_product = product.id";

            PreparedStatement ps2 = getPreparedStatment(sql2, false);
            ps2.setLong(1, purchase.getId());
            ResultSet rs2 = ps2.executeQuery();

            List<PurchaseDetail> purchaseDetailList = new ArrayList<PurchaseDetail>();
            while(rs2.next()) {
                PurchaseDetail purchaseDetail = new PurchaseDetail();
                purchaseDetail.setPrice(rs2.getBigDecimal("price"));
                purchaseDetail.setQuantity(rs2.getInt("quantity"));
                purchaseDetail.setPurchase(purchase);

                Product product = new Product();
                product.setId(rs2.getLong("id"));
                product.setName(rs2.getString("name"));

                purchaseDetail.setProduct(product);
                purchaseDetailList.add(purchaseDetail);
            }

            purchase.setPurchaseDetailList(purchaseDetailList);
            purchaseList.add(purchase);
        }

        return purchaseList;
    }

    @Override
    public void create(Purchase value) throws Exception {
        String sql = "INSERT INTO purchase (total, id_supplier) VALUES (?,?)";
        PreparedStatement ps = getPreparedStatment(sql, true);
        ps.setBigDecimal(1, value.getTotal());
        ps.setLong(2, value.getSupplier().getId());

        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        value.setId(rs.getLong(1));

        for (PurchaseDetail p: value.getPurchaseDetailList()) {
            p.setPurchase(value);
            new PurchaseDetailDao().create(p);
        }
    }

    @Override
    public void update(Purchase value) throws Exception {
        String sql = "UPDATE purchase SET total = ? WHERE id = ?";
        PreparedStatement ps = getPreparedStatment(sql, false);
        ps.setBigDecimal(1, value.getTotal());
        ps.setLong(2, value.getId());
        ps.executeUpdate();

        for(PurchaseDetail p: value.getPurchaseDetailList()) {
            new PurchaseDetailDao().update(p);
        }
    }

    @Override
    public void delete(Purchase value) throws Exception {
        for(PurchaseDetail p: value.getPurchaseDetailList()) {
            new PurchaseDetailDao().delete(p);
        }

        String sql = "DELETE FROM purchase WHERE id = ?";
        PreparedStatement ps = getPreparedStatment(sql, false);
        ps.setLong(1, value.getId());
        ps.executeUpdate();
    }
}

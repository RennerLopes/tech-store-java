package br.edu.femass.dao;

import br.edu.femass.model.Category;
import br.edu.femass.model.Product;
import br.edu.femass.model.Purchase;
import br.edu.femass.model.PurchaseDetail;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PurchaseDetailDao extends DaoPostgres implements Dao<PurchaseDetail>{
    @Override
    public List<PurchaseDetail> retrieve() throws Exception {
        String sql = "SELECT " +
                "product.id as product_id, " +
                "product.name as product_name, " +
                "product.purchasePrice as product_purchasePrice, " +
                "product.salePrice as product_salePrice, " +
                "product.stock as product_stock, " +
                "category.id as category_id, " +
                "category.name as category_name, " +
                "purchase.id as purchase_id, " +
                "purchase.date as purchase_date, " +
                "purchase.total as purchase_total, " +
                "purchase.id_supplier, " +
                "purchaseDetail.quantity, " +
                "purchaseDetail.price " +
                "FROM purchaseDetail " +
                "inner join product on product.id = purchaseDetail.id_product " +
                "inner join category on category.id = product.id_category " +
                "inner join purchase on purchase.id = purchaseDetail.id_purchase";
        PreparedStatement ps = getPreparedStatment(sql, false);
        ResultSet rs = ps.executeQuery();

        List<PurchaseDetail> purchaseDetailList = new ArrayList<PurchaseDetail>();
        while(rs.next()) {
            Product product = new Product();
            product.setId(rs.getLong("product_id"));
            product.setName(rs.getString("product_name"));
            product.setPurchasePrice(rs.getBigDecimal("product_purchasePrice"));
            product.setSalePrice(rs.getBigDecimal("product_salePrice"));
            product.setStock(rs.getInt("product_stock"));

            Category category = new Category();
            category.setId(rs.getLong("category_id"));
            category.setName(rs.getString("category_name"));

            Purchase purchase = new Purchase();
            purchase.setId(rs.getLong("purchase_id"));

            PurchaseDetail purchaseDetail = new PurchaseDetail();
            purchaseDetail.setQuantity(rs.getInt("quantity"));
            purchaseDetail.setPrice(rs.getBigDecimal("price"));
            purchaseDetail.setProduct(product);
            purchaseDetail.setPurchase(purchase);

            purchaseDetailList.add(purchaseDetail);
        }

        return purchaseDetailList;
    }

    @Override
    public void create(PurchaseDetail value) throws Exception {
        String sql = "INSERT INTO purchaseDetail (quantity, price, id_purchase, id_product ) VALUES (?,?,?,?)";
        PreparedStatement ps = getPreparedStatment(sql, false);
        ps.setInt(1, value.getQuantity());
        ps.setBigDecimal(2, value.getPrice());
        ps.setLong(3, value.getPurchase().getId());
        ps.setLong(4, value.getProduct().getId());

        Product product = value.getProduct();
        product.setStock(product.getStock() + value.getQuantity());

        new ProductDao().update(product);
        ps.executeUpdate();

    }

    @Override
    public void update(PurchaseDetail value) throws Exception {
        String sql = "UPDATE purchaseDetail SET quantity = ?, price = ? WHERE id_purchase = ? AND id_product = ?";
        PreparedStatement ps = getPreparedStatment(sql, false);
        ps.setInt(1, value.getQuantity());
        ps.setBigDecimal(2, value.getPrice());
        ps.setLong(3, value.getPurchase().getId());
        ps.setLong(4, value.getProduct().getId());
        ps.executeUpdate();
    }

    @Override
    public void delete(PurchaseDetail value) throws Exception {
        String sql = "DELETE FROM purchaseDetail WHERE id_purchase = ? AND id_product = ?";
        PreparedStatement ps = getPreparedStatment(sql, false);
        ps.setLong(1, value.getPurchase().getId());
        ps.setLong(2, value.getProduct().getId());

        Product product = value.getProduct();
        product.setStock(product.getStock() - value.getQuantity());

        new ProductDao().update(product);
        ps.executeUpdate();

    }
}

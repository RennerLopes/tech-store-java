package br.edu.femass.dao;

import br.edu.femass.model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SaleDetailDao extends DaoPostgres implements Dao<SaleDetail>{
    @Override
    public List<SaleDetail> retrieve() throws Exception {
        String sql = "SELECT " +
                "product.id as product_id, " +
                "product.name as product_name, " +
                "product.purchasePrice as product_purchasePrice, " +
                "product.salePrice as product_salePrice, " +
                "product.stock as product_stock, " +
                "category.id as category_id, " +
                "category.name as category_name, " +
                "sale.id as sale_id, " +
                "sale.date as sale_date, " +
                "sale.total as sale_total, " +
                "sale.id_supplier, " +
                "saleDetail.quantity, " +
                "saleDetail.price " +
                "FROM saleDetail " +
                "inner join product on product.id = saleDetail.id_product " +
                "inner join category on category.id = product.id_category " +
                "inner join sale on sale.id = saleDetail.id_sale";
        PreparedStatement ps = getPreparedStatment(sql, false);
        ResultSet rs = ps.executeQuery();

        List<SaleDetail> saleDetailList = new ArrayList<SaleDetail>();
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

            Sale sale = new Sale();
            sale.setId(rs.getLong("sale_id"));

            SaleDetail saleDetail = new SaleDetail();
            saleDetail.setQuantity(rs.getInt("quantity"));
            saleDetail.setPrice(rs.getBigDecimal("price"));
            saleDetail.setProduct(product);
            saleDetail.setSale(sale);

            saleDetailList.add(saleDetail);
        }

        return saleDetailList;
    }

    @Override
    public void create(SaleDetail value) throws Exception {
        String sql = "INSERT INTO saleDetail (quantity, price, id_sale, id_product ) VALUES (?,?,?,?)";
        PreparedStatement ps = getPreparedStatment(sql, false);
        ps.setInt(1, value.getQuantity());
        ps.setBigDecimal(2, value.getPrice());
        ps.setLong(3, value.getSale().getId());
        ps.setLong(4, value.getProduct().getId());

        Product product = value.getProduct();
        product.setStock(product.getStock() - value.getQuantity());

        new ProductDao().update(product);

        ps.executeUpdate();
    }

    @Override
    public void update(SaleDetail value) throws Exception {
        String sql = "UPDATE saleDetail SET quantity = ?, price = ? WHERE id_sale = ? AND id_product = ?";
        PreparedStatement ps = getPreparedStatment(sql, false);
        ps.setInt(1, value.getQuantity());
        ps.setBigDecimal(2, value.getPrice());
        ps.setLong(3, value.getSale().getId());
        ps.setLong(4, value.getProduct().getId());
        ps.executeUpdate();
    }

    @Override
    public void delete(SaleDetail value) throws Exception {
        String sql = "DELETE FROM saleDetail WHERE id_sale = ? AND id_product = ?";
        PreparedStatement ps = getPreparedStatment(sql, false);
        ps.setLong(1, value.getSale().getId());
        ps.setLong(2, value.getProduct().getId());

        Product product = value.getProduct();
        product.setStock(product.getStock() + value.getQuantity());

        new ProductDao().update(product);
        ps.executeUpdate();
    }
}

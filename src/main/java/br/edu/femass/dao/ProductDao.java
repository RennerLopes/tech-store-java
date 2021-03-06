package br.edu.femass.dao;


import br.edu.femass.model.Category;
import br.edu.femass.model.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductDao extends DaoPostgres implements Dao<Product>{
    @Override
    public List<Product> retrieve() throws Exception {
        String sql = "SELECT " +
                "product.id as product_id, " +
                "product.name as product_name, " +
                "product.purchasePrice as product_purchasePrice, " +
                "product.stock as product_stock, " +
                "product.salePrice as product_salePrice, " +
                "category.id as category_id, " +
                "category.name as category_name " +
                "FROM product, category " +
                "WHERE product.id_category = category.id " +
                " ORDER BY product_name";
        PreparedStatement ps = getPreparedStatment(sql, false);
        ResultSet rs = ps.executeQuery();

        List<Product> products = new ArrayList<Product>();
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
            product.setCategory(category);
            products.add(product);
        }

        return products;
    }

    @Override
    public void create(Product value) throws Exception {
        String sql = "INSERT INTO product (name, purchasePrice, stock, id_category, salePrice) VALUES (?,?,?,?,?)";
        PreparedStatement ps = getPreparedStatment(sql, true);
        ps.setString(1, value.getName());
        ps.setBigDecimal(2, value.getPurchasePrice());
        ps.setInt(3, value.getStock());
        ps.setLong(4, value.getCategory().getId());
        ps.setBigDecimal(5, value.getSalePrice());


        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        value.setId(rs.getLong(1));
    }

    @Override
    public void update(Product value) throws Exception {
        String sql = "UPDATE product SET name = ?, purchasePrice = ?, stock = ?, id_category = ?, salePrice = ? WHERE id = ?";
        PreparedStatement ps = getPreparedStatment(sql, false);
        ps.setString(1, value.getName());
        ps.setBigDecimal(2, value.getPurchasePrice());
        ps.setInt(3, value.getStock());
        ps.setLong(4, value.getCategory().getId());
        ps.setBigDecimal(5, value.getSalePrice());
        ps.setLong(6, value.getId());

        ps.executeUpdate();
    }

    @Override
    public void delete(Product value) throws Exception {
        String sql = "DELETE FROM product WHERE id = ?";
        PreparedStatement ps = getPreparedStatment(sql, false);
        ps.setLong(1, value.getId());
        ps.executeUpdate();
    }
}

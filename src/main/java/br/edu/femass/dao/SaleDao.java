package br.edu.femass.dao;

import br.edu.femass.model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SaleDao extends DaoPostgres implements Dao<Sale>{
    @Override
    public List<Sale> retrieve() throws Exception {
        String sql = "SELECT " +
                "sale.id as sale_id, " +
                "sale.date as sale_date, " +
                "sale.total as sale_total, " +
                "client.id as client_id, " +
                "client.name as client_name, " +
                "client.cpf as client_cpf, " +
                "client.phone as client_phone, " +
                "client.address as client_address " +
                "FROM sale " +
                "INNER JOIN client ON client.id = sale.id_client";
        PreparedStatement ps = getPreparedStatment(sql, false);
        ResultSet rs = ps.executeQuery();

        List<Sale> saleList = new ArrayList<Sale>();
        while(rs.next()) {
            Sale sale = new Sale();
            sale.setId(rs.getLong("sale_id"));
            sale.setDate(rs.getDate("sale_date"));
            sale.setTotal(rs.getBigDecimal("sale_total"));

            Client client = new Client();
            client.setId(rs.getLong("client_id"));
            client.setAddress(rs.getString("client_address"));
            client.setPhone(rs.getString("client_phone"));
            client.setName(rs.getString("client_name"));
            client.setCpf(rs.getString("client_cpf"));
            sale.setClient(client);

            String sql2 = "SELECT " +
                    "saleDetail.quantity, " +
                    "saleDetail.price, " +
                    "product.id, " +
                    "product.name, " +
                    "product.purchasePrice, " +
                    "product.stock, " +
                    "product.id_category, " +
                    "product.salePrice " +
                    "FROM saleDetail, product " +
                    "WHERE saleDetail.id_sale = ? AND " +
                    "saleDetail.id_product = product.id";

            PreparedStatement ps2 = getPreparedStatment(sql2, false);
            ps2.setLong(1, sale.getId());
            ResultSet rs2 = ps2.executeQuery();

            List<SaleDetail> saleDetailList = new ArrayList<SaleDetail>();
            while(rs2.next()) {
                SaleDetail saleDetail = new SaleDetail();
                saleDetail.setPrice(rs2.getBigDecimal("price"));
                saleDetail.setQuantity(rs2.getInt("quantity"));
                saleDetail.setSale(sale);

                Product product = new Product();
                product.setId(rs2.getLong("id"));
                product.setName(rs2.getString("name"));
                product.setStock(rs2.getInt("stock"));
                product.setSalePrice(rs2.getBigDecimal("salePrice"));
                product.setPurchasePrice(rs2.getBigDecimal("purchasePrice"));

                Category category = new Category();
                category.setId(rs2.getLong("id_category"));
                product.setCategory(category);

                saleDetail.setProduct(product);
                saleDetailList.add(saleDetail);
            }

            sale.setSaleDetailList(saleDetailList);
            saleList.add(sale);
        }

        return saleList;
    }

    @Override
    public void create(Sale value) throws Exception {
        String sql = "INSERT INTO sale (total, id_client) VALUES (?,?)";
        PreparedStatement ps = getPreparedStatment(sql, true);
        ps.setBigDecimal(1, value.getTotal());
        ps.setLong(2, value.getClient().getId());

        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        value.setId(rs.getLong(1));

        for (SaleDetail p: value.getSaleDetailList()) {
            p.setSale(value);
            new SaleDetailDao().create(p);
        }
    }

    @Override
    public void update(Sale value) throws Exception {
        String sql = "UPDATE sale SET total = ? WHERE id = ?";
        PreparedStatement ps = getPreparedStatment(sql, false);
        ps.setBigDecimal(1, value.getTotal());
        ps.setLong(2, value.getId());
        ps.executeUpdate();

        for(SaleDetail p: value.getSaleDetailList()) {
            new SaleDetailDao().update(p);
        }
    }

    @Override
    public void delete(Sale value) throws Exception {
        for(SaleDetail p: value.getSaleDetailList()) {
            new SaleDetailDao().delete(p);
        }

        String sql = "DELETE FROM sale WHERE id = ?";
        PreparedStatement ps = getPreparedStatment(sql, false);
        ps.setLong(1, value.getId());
        ps.executeUpdate();
    }
}

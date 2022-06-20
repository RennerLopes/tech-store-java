package br.edu.femass.dao;

import br.edu.femass.model.Supplier;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SupplierDao extends DaoPostgres implements Dao<Supplier>{
    @Override
    public List<Supplier> retrieve() throws Exception {
        String sql = "SELECT * FROM supplier ORDER BY name";
        PreparedStatement ps = getPreparedStatment(sql, false);
        ResultSet rs = ps.executeQuery();

        List<Supplier> suppliers = new ArrayList<Supplier>();
        while(rs.next()) {
            Supplier supplier = new Supplier();
            supplier.setName(rs.getString("name"));
            supplier.setCnpj(rs.getString("cnpj"));
            supplier.setPhone(rs.getString("phone"));
            supplier.setAddress(rs.getString("address"));
            supplier.setId(rs.getLong("id"));
            suppliers.add(supplier);
        }

        return suppliers;
    }

    @Override
    public void create(Supplier value) throws Exception {
        String sql = "INSERT INTO supplier (name, cnpj, phone, address) VALUES (?,?,?,?)";
        PreparedStatement ps = getPreparedStatment(sql, true);
        ps.setString(1, value.getName());
        ps.setString(2, value.getCnpj());
        ps.setString(3, value.getPhone());
        ps.setString(4, value.getAddress());

        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        value.setId(rs.getLong(1));
    }

    @Override
    public void update(Supplier value) throws Exception {
        String sql = "UPDATE supplier SET name = ?, cnpj = ?, phone = ?, address = ? WHERE id = ?";
        PreparedStatement ps = getPreparedStatment(sql, false);
        ps.setString(1, value.getName());
        ps.setString(2, value.getCnpj());
        ps.setString(3, value.getPhone());
        ps.setString(4, value.getAddress());
        ps.setLong(5, value.getId());
        ps.executeUpdate();
    }

    @Override
    public void delete(Supplier value) throws Exception {
        String sql = "DELETE FROM supplier WHERE id = ?";
        PreparedStatement ps = getPreparedStatment(sql, false);
        ps.setLong(1, value.getId());
        ps.executeUpdate();
    }
}

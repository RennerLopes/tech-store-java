package br.edu.femass.dao;

import br.edu.femass.model.Client;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClientDao extends DaoPostgres implements Dao<Client>{
    @Override
    public List<Client> retrieve() throws Exception {
        String sql = "SELECT * FROM client ORDER BY name";
        PreparedStatement ps = getPreparedStatment(sql, false);
        ResultSet rs = ps.executeQuery();

        List<Client> clients = new ArrayList<Client>();
        while(rs.next()) {
            Client client = new Client();
            client.setName(rs.getString("name"));
            client.setCpf(rs.getString("cpf"));
            client.setPhone(rs.getString("phone"));
            client.setAddress(rs.getString("address"));
            client.setId(rs.getLong("id"));
            clients.add(client);
        }

        return clients;
    }

    @Override
    public void create(Client value) throws Exception {
        String sql = "INSERT INTO client (name, cpf, phone, address) VALUES (?,?,?,?)";
        PreparedStatement ps = getPreparedStatment(sql, true);
        ps.setString(1, value.getName());
        ps.setString(2, value.getCpf());
        ps.setString(3, value.getPhone());
        ps.setString(4, value.getAddress());

        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        value.setId(rs.getLong(1));
    }

    @Override
    public void update(Client value) throws Exception {
        String sql = "UPDATE client SET name = ?, cpf = ?, phone = ?, address = ? WHERE id = ?";
        PreparedStatement ps = getPreparedStatment(sql, false);
        ps.setString(1, value.getName());
        ps.setString(2, value.getCpf());
        ps.setString(3, value.getPhone());
        ps.setString(4, value.getAddress());
        ps.setLong(5, value.getId());
        ps.executeUpdate();
    }

    @Override
    public void delete(Client value) throws Exception {
        String sql = "DELETE FROM client WHERE id = ?";
        PreparedStatement ps = getPreparedStatment(sql, false);
        ps.setLong(1, value.getId());
        ps.executeUpdate();
    }
}

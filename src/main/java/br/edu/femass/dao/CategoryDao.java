package br.edu.femass.dao;

import br.edu.femass.model.Category;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CategoryDao extends DaoPostgres implements Dao<Category>{
    @Override
    public List<Category> retrieve() throws Exception {
        String sql = "SELECT * FROM category ORDER BY name";
        PreparedStatement ps = getPreparedStatment(sql, false);
        ResultSet rs = ps.executeQuery();

        List<Category> categories = new ArrayList<Category>();
        while(rs.next()) {
            Category category = new Category();
            category.setName(rs.getString("name"));
            category.setId(rs.getLong("id"));
            categories.add(category);
        }

        return categories;
    }

    @Override
    public void create(Category value) throws Exception {
        String sql = "INSERT INTO category (name) VALUES (?)";
        PreparedStatement ps = getPreparedStatment(sql, true);
        ps.setString(1, value.getName());

        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        value.setId(rs.getLong(1));
    }

    @Override
    public void update(Category value) throws Exception {
        String sql = "UPDATE category SET name = ? WHERE id = ?";
        PreparedStatement ps = getPreparedStatment(sql, false);
        ps.setString(1, value.getName());
        ps.setLong(2, value.getId());
        ps.executeUpdate();
    }

    @Override
    public void delete(Category value) throws Exception {
        String sql = "DELETE FROM category WHERE id = ?";
        PreparedStatement ps = getPreparedStatment(sql, false);
        ps.setLong(1, value.getId());
        ps.executeUpdate();
    }
}

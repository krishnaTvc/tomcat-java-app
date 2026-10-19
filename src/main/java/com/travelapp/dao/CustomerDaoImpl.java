package com.travelapp.dao;

import com.travelapp.bean.CustomerBean;
import com.travelapp.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of CustomerDao for MySQL and SQLite.
 * Operates on the `customer_data` table.
 */
public class CustomerDaoImpl implements CustomerDao {
    private static final Logger LOGGER = Logger.getLogger(CustomerDaoImpl.class.getName());

    private static final String INSERT_CUSTOMER_SQL =
            "INSERT INTO customer_data (name, age, gender, bmi, desired_country) VALUES (?, ?, ?, ?, ?)";

    private static final String SELECT_ALL_CUSTOMERS_SQL =
            "SELECT id, name, age, gender, bmi, desired_country, created_at FROM customer_data ORDER BY id DESC";

    private static final String SELECT_CUSTOMER_BY_ID_SQL =
            "SELECT id, name, age, gender, bmi, desired_country, created_at FROM customer_data WHERE id = ?";

    private static final String COUNT_CUSTOMERS_SQL =
            "SELECT COUNT(*) FROM customer_data";

    @Override
    public boolean saveCustomer(CustomerBean customer) throws Exception {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(INSERT_CUSTOMER_SQL, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, customer.getName());
            ps.setInt(2, customer.getAge());
            ps.setString(3, customer.getGender());
            ps.setDouble(4, customer.getBmi());
            ps.setString(5, customer.getDesiredCountry());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                rs = ps.getGeneratedKeys();
                if (rs != null && rs.next()) {
                    customer.setId(rs.getInt(1));
                }
                LOGGER.info("Customer saved successfully with ID: " + customer.getId());
                return true;
            }
            return false;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error inserting customer into customer_data: " + e.getMessage(), e);
            throw e;
        } finally {
            DBConnection.close(rs, ps, conn);
        }
    }

    @Override
    public List<CustomerBean> getAllCustomers() throws Exception {
        List<CustomerBean> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(SELECT_ALL_CUSTOMERS_SQL);
            rs = ps.executeQuery();

            while (rs.next()) {
                CustomerBean customer = mapRowToCustomer(rs);
                list.add(customer);
            }
            return list;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving customers: " + e.getMessage(), e);
            throw e;
        } finally {
            DBConnection.close(rs, ps, conn);
        }
    }

    @Override
    public CustomerBean getCustomerById(int id) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(SELECT_CUSTOMER_BY_ID_SQL);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                return mapRowToCustomer(rs);
            }
            return null;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving customer by id " + id + ": " + e.getMessage(), e);
            throw e;
        } finally {
            DBConnection.close(rs, ps, conn);
        }
    }

    @Override
    public int getCustomerCount() throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(COUNT_CUSTOMERS_SQL);
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error counting customers: " + e.getMessage(), e);
            throw e;
        } finally {
            DBConnection.close(rs, ps, conn);
        }
    }

    private CustomerBean mapRowToCustomer(ResultSet rs) throws Exception {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        int age = rs.getInt("age");
        String gender = rs.getString("gender");
        double bmi = rs.getDouble("bmi");
        String desiredCountry = rs.getString("desired_country");
        Timestamp createdAt = null;
        try {
            createdAt = rs.getTimestamp("created_at");
        } catch (Exception ignored) {
            // In SQLite, timestamp might be stored as text
        }

        return new CustomerBean(id, name, age, gender, bmi, desiredCountry, createdAt);
    }
}

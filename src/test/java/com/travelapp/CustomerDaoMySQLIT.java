package com.travelapp;

import com.travelapp.bean.CustomerBean;
import com.travelapp.dao.CustomerDao;
import com.travelapp.dao.CustomerDaoImpl;
import com.travelapp.util.DBConnection;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration Test for verifying actual MySQL database connectivity and operations.
 * Runs during `mvn verify` (Failsafe phase) or when DB_HOST is explicitly configured.
 */
public class CustomerDaoMySQLIT {

    private static CustomerDao customerDao;
    private static boolean mysqlAvailable = false;

    @BeforeAll
    public static void setUp() {
        // Enforce MySQL mode for this integration test
        System.setProperty("db.type", "mysql");

        try (Connection conn = DBConnection.getConnection()) {
            mysqlAvailable = (conn != null && !conn.isClosed());
            customerDao = new CustomerDaoImpl();
        } catch (Exception e) {
            System.out.println("[CustomerDaoMySQLIT] Live MySQL instance not reachable (" + e.getMessage() +
                    "). Integration test skipped. Configure DB_HOST and DB_USER to run against live AWS EC2 MySQL.");
            mysqlAvailable = false;
        }
    }

    @Test
    public void testMySQLConnectivityAndCustomerCRUD() throws Exception {
        Assumptions.assumeTrue(mysqlAvailable, "MySQL is not reachable, skipping live MySQL integration test.");

        // Create a test customer
        CustomerBean customer = new CustomerBean();
        customer.setName("Integration Test Traveler");
        customer.setAge(32);
        customer.setGender("Male");
        customer.setBmi(23.50);
        customer.setDesiredCountry("Iceland");

        // 1. Verify insertion into customer_data in MySQL
        boolean saved = customerDao.saveCustomer(customer);
        assertTrue(saved, "Customer should be saved into live MySQL customer_data table");
        assertNotNull(customer.getId(), "MySQL auto-increment ID must be generated");

        // 2. Verify retrieval from live MySQL
        CustomerBean retrieved = customerDao.getCustomerById(customer.getId());
        assertNotNull(retrieved, "Should retrieve the newly inserted traveler from MySQL");
        assertEquals("Integration Test Traveler", retrieved.getName());
        assertEquals("Iceland", retrieved.getDesiredCountry());

        // 3. Verify total count
        List<CustomerBean> all = customerDao.getAllCustomers();
        assertFalse(all.isEmpty(), "MySQL customer_data list should not be empty");
    }
}

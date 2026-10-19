package com.travelapp;

import com.travelapp.bean.CustomerBean;
import com.travelapp.dao.CustomerDao;
import com.travelapp.dao.CustomerDaoImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerDaoTest {

    private static CustomerDao customerDao;

    @BeforeAll
    public static void setUp() {
        // Use SQLite for local test isolation without needing an active MySQL EC2 instance
        System.setProperty("db.type", "sqlite");
        new File("target").mkdirs();
        System.setProperty("sqlite.filepath", "target/test_travel_app.db");
        customerDao = new CustomerDaoImpl();
    }

    @Test
    public void testSaveAndRetrieveCustomer() throws Exception {
        CustomerBean customer = new CustomerBean();
        customer.setName("Elena Rostova");
        customer.setAge(29);
        customer.setGender("Female");
        customer.setBmi(21.5);
        customer.setDesiredCountry("Switzerland");

        boolean saved = customerDao.saveCustomer(customer);
        assertTrue(saved, "Customer should be saved successfully");
        assertNotNull(customer.getId(), "Generated customer ID should not be null");
        assertTrue(customer.getId() > 0, "Generated customer ID should be positive");

        // Verify retrieval by ID
        CustomerBean retrieved = customerDao.getCustomerById(customer.getId());
        assertNotNull(retrieved, "Retrieved customer should not be null");
        assertEquals("Elena Rostova", retrieved.getName());
        assertEquals("Switzerland", retrieved.getDesiredCountry());
        assertEquals(29, retrieved.getAge());
        assertEquals("Female", retrieved.getGender());

        // Verify list retrieval
        List<CustomerBean> all = customerDao.getAllCustomers();
        assertFalse(all.isEmpty(), "Customer list should not be empty");

        // Verify count
        int count = customerDao.getCustomerCount();
        assertTrue(count >= 1, "Customer count should be at least 1");
    }
}

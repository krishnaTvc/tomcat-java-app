package com.travelapp.dao;

import com.travelapp.bean.CustomerBean;
import java.util.List;

/**
 * Data Access Object (DAO) interface for Customer operations.
 */
public interface CustomerDao {
    /**
     * Inserts a new customer into the `customer_data` table.
     * Sets the auto-generated ID on the customer bean.
     * 
     * @param customer CustomerBean containing registration details
     * @return true if inserted successfully, false otherwise
     */
    boolean saveCustomer(CustomerBean customer) throws Exception;

    /**
     * Retrieves all registered customers from `customer_data`.
     * 
     * @return list of CustomerBean instances
     */
    List<CustomerBean> getAllCustomers() throws Exception;

    /**
     * Retrieves a customer by their unique ID.
     * 
     * @param id customer ID
     * @return CustomerBean if found, null otherwise
     */
    CustomerBean getCustomerById(int id) throws Exception;

    /**
     * Returns total count of registered customers.
     */
    int getCustomerCount() throws Exception;
}

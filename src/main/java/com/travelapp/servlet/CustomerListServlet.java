package com.travelapp.servlet;

import com.travelapp.bean.CustomerBean;
import com.travelapp.dao.CustomerDao;
import com.travelapp.dao.CustomerDaoImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller servlet for querying and displaying customer records from `customer_data`.
 */
@WebServlet(name = "CustomerListServlet", urlPatterns = {"/customers", "/CustomerListServlet"})
public class CustomerListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(CustomerListServlet.class.getName());

    private CustomerDao customerDao;

    @Override
    public void init() throws ServletException {
        super.init();
        this.customerDao = new CustomerDaoImpl();
        LOGGER.info("CustomerListServlet initialized.");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<CustomerBean> customers = customerDao.getAllCustomers();
            int totalCount = customerDao.getCustomerCount();

            request.setAttribute("customers", customers);
            request.setAttribute("totalCount", totalCount);
            request.getRequestDispatcher("/customers.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load customer list: " + e.getMessage(), e);
            request.setAttribute("errorMessage", "Unable to retrieve customers from database: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}

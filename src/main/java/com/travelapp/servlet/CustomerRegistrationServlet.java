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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller servlet handling customer registration.
 * Validates form input, creates CustomerBean, and persists via CustomerDao.
 */
@WebServlet(name = "CustomerRegistrationServlet", urlPatterns = {"/register", "/CustomerRegistrationServlet"})
public class CustomerRegistrationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(CustomerRegistrationServlet.class.getName());

    private CustomerDao customerDao;

    @Override
    public void init() throws ServletException {
        super.init();
        this.customerDao = new CustomerDaoImpl();
        LOGGER.info("CustomerRegistrationServlet initialized.");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward GET requests to registration page
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String name = request.getParameter("name");
        String ageStr = request.getParameter("age");
        String gender = request.getParameter("gender");
        String bmiStr = request.getParameter("bmi");
        String weightStr = request.getParameter("weight");
        String heightStr = request.getParameter("height");
        String desiredCountry = request.getParameter("desiredCountry");

        // Keep form values in case of validation failure
        request.setAttribute("formName", name);
        request.setAttribute("formAge", ageStr);
        request.setAttribute("formGender", gender);
        request.setAttribute("formBmi", bmiStr);
        request.setAttribute("formDesiredCountry", desiredCountry);

        // Validation Checks
        StringBuilder errorMessage = new StringBuilder();

        if (name == null || name.trim().isEmpty()) {
            errorMessage.append("Full Name is required. ");
        }

        int age = 0;
        try {
            if (ageStr == null || ageStr.trim().isEmpty()) {
                errorMessage.append("Age is required. ");
            } else {
                age = Integer.parseInt(ageStr.trim());
                if (age <= 0 || age > 120) {
                    errorMessage.append("Please enter a valid age between 1 and 120. ");
                }
            }
        } catch (NumberFormatException e) {
            errorMessage.append("Age must be a valid number. ");
        }

        if (gender == null || gender.trim().isEmpty()) {
            errorMessage.append("Gender selection is required. ");
        }

        // Calculate or parse BMI
        double bmi = 0.0;
        try {
            if (bmiStr != null && !bmiStr.trim().isEmpty()) {
                bmi = Double.parseDouble(bmiStr.trim());
            } else if (weightStr != null && !weightStr.trim().isEmpty() &&
                       heightStr != null && !heightStr.trim().isEmpty()) {
                double weight = Double.parseDouble(weightStr.trim()); // kg
                double heightCm = Double.parseDouble(heightStr.trim()); // cm
                if (heightCm > 0 && weight > 0) {
                    double heightM = heightCm / 100.0;
                    bmi = weight / (heightM * heightM);
                }
            }

            if (bmi <= 0 || bmi > 100) {
                errorMessage.append("Please provide a valid BMI (or enter valid height & weight). ");
            }
        } catch (NumberFormatException e) {
            errorMessage.append("BMI / Height / Weight must be valid numbers. ");
        }

        // Desired country is mandatory
        if (desiredCountry == null || desiredCountry.trim().isEmpty()) {
            errorMessage.append("Desired Country to Travel is mandatory. ");
        }

        // If validation errors exist, forward back to registration page
        if (errorMessage.length() > 0) {
            request.setAttribute("errorMessage", errorMessage.toString());
            request.getRequestDispatcher("/index.jsp").forward(request, response);
            return;
        }

        // Construct JavaBean
        CustomerBean customer = new CustomerBean();
        customer.setName(name.trim());
        customer.setAge(age);
        customer.setGender(gender.trim());
        customer.setBmi(bmi);
        customer.setDesiredCountry(desiredCountry.trim());

        try {
            boolean isSaved = customerDao.saveCustomer(customer);
            if (isSaved) {
                request.setAttribute("customer", customer);
                request.setAttribute("successMessage", "Registration completed successfully! Welcome aboard, " + customer.getName() + ".");
                request.getRequestDispatcher("/confirmation.jsp").forward(request, response);
            } else {
                request.setAttribute("errorMessage", "Could not save customer record to database. Please try again.");
                request.getRequestDispatcher("/index.jsp").forward(request, response);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Database error during registration: " + e.getMessage(), e);
            request.setAttribute("errorMessage", "Database Connection/Execution Error: " + e.getMessage() +
                    ". Please verify that your MySQL EC2 instance or SQLite database is reachable.");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }
}

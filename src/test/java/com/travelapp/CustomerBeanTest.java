package com.travelapp;

import com.travelapp.bean.CustomerBean;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerBeanTest {

    @Test
    public void testCustomerBeanProperties() {
        CustomerBean customer = new CustomerBean();
        customer.setName("John Doe");
        customer.setAge(30);
        customer.setGender("Male");
        customer.setBmi(22.456);
        customer.setDesiredCountry("Japan");

        assertEquals("John Doe", customer.getName());
        assertEquals(30, customer.getAge());
        assertEquals("Male", customer.getGender());
        assertEquals(22.46, customer.getBmi(), 0.001, "BMI should be rounded to 2 decimal places");
        assertEquals("Japan", customer.getDesiredCountry());
    }

    @Test
    public void testBmiCategoryClassification() {
        CustomerBean under = new CustomerBean("A", 20, "Female", 17.5, "France");
        assertEquals("Underweight", under.getBmiCategory());
        assertEquals("badge-info", under.getBmiBadgeClass());

        CustomerBean normal = new CustomerBean("B", 25, "Male", 22.0, "Italy");
        assertEquals("Normal weight", normal.getBmiCategory());
        assertEquals("badge-success", normal.getBmiBadgeClass());

        CustomerBean over = new CustomerBean("C", 35, "Female", 27.5, "Spain");
        assertEquals("Overweight", over.getBmiCategory());
        assertEquals("badge-warning", over.getBmiBadgeClass());

        CustomerBean obese = new CustomerBean("D", 45, "Male", 32.0, "Norway");
        assertEquals("Obese", obese.getBmiCategory());
        assertEquals("badge-danger", obese.getBmiBadgeClass());
    }
}

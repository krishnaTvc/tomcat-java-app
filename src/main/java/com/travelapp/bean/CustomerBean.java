package com.travelapp.bean;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * JavaBean representing customer registration data.
 * Corresponds to the `customer_data` table in the database.
 */
public class CustomerBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;
    private int age;
    private String gender;
    private double bmi;
    private String desiredCountry;
    private Timestamp createdAt;

    // Default No-Argument Constructor (JavaBean specification)
    public CustomerBean() {
    }

    // Parameterized Constructor (without ID for registration)
    public CustomerBean(String name, int age, String gender, double bmi, String desiredCountry) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.bmi = bmi;
        this.desiredCountry = desiredCountry;
    }

    // Full Parameterized Constructor
    public CustomerBean(Integer id, String name, int age, String gender, double bmi, String desiredCountry, Timestamp createdAt) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.bmi = bmi;
        this.desiredCountry = desiredCountry;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name != null ? name.trim() : null;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender != null ? gender.trim() : null;
    }

    public double getBmi() {
        return bmi;
    }

    public void setBmi(double bmi) {
        this.bmi = Math.round(bmi * 100.0) / 100.0; // round to 2 decimal places
    }

    public String getDesiredCountry() {
        return desiredCountry;
    }

    public void setDesiredCountry(String desiredCountry) {
        this.desiredCountry = desiredCountry != null ? desiredCountry.trim() : null;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Categorizes BMI value according to standard WHO guidelines.
     */
    public String getBmiCategory() {
        if (bmi < 18.5) {
            return "Underweight";
        } else if (bmi < 25.0) {
            return "Normal weight";
        } else if (bmi < 30.0) {
            return "Overweight";
        } else {
            return "Obese";
        }
    }

    public String getBmiBadgeClass() {
        if (bmi < 18.5) {
            return "badge-info";
        } else if (bmi < 25.0) {
            return "badge-success";
        } else if (bmi < 30.0) {
            return "badge-warning";
        } else {
            return "badge-danger";
        }
    }

    @Override
    public String toString() {
        return "CustomerBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", bmi=" + bmi +
                ", desiredCountry='" + desiredCountry + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}

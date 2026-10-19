<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registration Confirmed - Travel App</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <!-- Navigation Bar -->
    <header class="navbar">
        <div class="nav-container">
            <a href="${pageContext.request.contextPath}/index.jsp" class="brand">
                <div class="brand-icon">&#9992;</div>
                <span>Travel App</span>
            </a>
            <ul class="nav-links">
                <li><a href="${pageContext.request.contextPath}/index.jsp" class="nav-link">Register</a></li>
                <li><a href="${pageContext.request.contextPath}/customers" class="nav-link">Travelers List</a></li>
                <li><a href="${pageContext.request.contextPath}/customers" class="nav-btn">View customer_data</a></li>
            </ul>
        </div>
    </header>

    <!-- Main Content -->
    <main class="main-wrapper">
        <div class="glass-card confirmation-card">
            
            <div class="success-icon-bubble">&#10004;</div>
            <h1 style="font-size: 2rem; font-weight: 800; color: #0f172a; margin-bottom: 0.5rem;">Registration Successful!</h1>
            <p style="color: #64748b; font-size: 1.05rem;">
                Your travel profile has been stored into the <strong style="color: #4f46e5;">customer_data</strong> table on the AWS EC2 MySQL instance.
            </p>

            <c:choose>
                <c:when test="${not empty customer}">
                    <!-- Boarding / Registration Pass -->
                    <div class="ticket-container">
                        <div class="ticket-header">
                            <div>
                                <span style="font-size: 0.8rem; text-transform: uppercase; letter-spacing: 0.05em; opacity: 0.85;">Official Traveler Record</span>
                                <h3 style="font-size: 1.25rem; font-weight: 700; margin-top: 0.15rem;">${customer.name}</h3>
                            </div>
                            <div>
                                <span class="ticket-id">DB ID: #${customer.id != null ? customer.id : 'N/A'}</span>
                            </div>
                        </div>

                        <div class="ticket-body">
                            <div class="ticket-field">
                                <span class="ticket-field-label">Age</span>
                                <span class="ticket-field-value">${customer.age} Years</span>
                            </div>

                            <div class="ticket-field">
                                <span class="ticket-field-label">Gender</span>
                                <span class="ticket-field-value">${customer.gender}</span>
                            </div>

                            <div class="ticket-field">
                                <span class="ticket-field-label">Body Mass Index (BMI)</span>
                                <span class="ticket-field-value">
                                    ${customer.bmi} 
                                    <span class="badge ${customer.bmiBadgeClass}">${customer.bmiCategory}</span>
                                </span>
                            </div>

                            <div class="ticket-field">
                                <span class="ticket-field-label">Desired Destination</span>
                                <span class="ticket-field-value" style="color: #4f46e5;">
                                    &#127758; ${customer.desiredCountry}
                                </span>
                            </div>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <p style="margin: 2rem 0; color: #64748b;">No customer data found in current session context.</p>
                </c:otherwise>
            </c:choose>

            <div class="ticket-actions">
                <a href="${pageContext.request.contextPath}/index.jsp" class="btn-secondary">
                    <span>&#43;</span> Register Another Traveler
                </a>
                <a href="${pageContext.request.contextPath}/customers" class="btn-primary">
                    <span>&#128065;</span> View All in Database
                </a>
            </div>

        </div>
    </main>

    <!-- Footer -->
    <footer>
        <div class="footer-content">
            <div>
                <strong>Travel App</strong> &bull; Servlet 6.0 &bull; Tomcat 10 &bull; MySQL on AWS EC2
            </div>
            <div>
                Table: <code>customer_data</code> &bull; Built with Jakarta EE &amp; Jenkins
            </div>
        </div>
    </footer>

</body>
</html>

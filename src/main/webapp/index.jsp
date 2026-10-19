<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Travel App - Register your journey profile with health and destination details.">
    <title>Travel App - Registration</title>
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
                <li><a href="${pageContext.request.contextPath}/index.jsp" class="nav-link active">Register</a></li>
                <li><a href="${pageContext.request.contextPath}/customers" class="nav-link">Travelers List</a></li>
                <li><a href="${pageContext.request.contextPath}/customers" class="nav-btn">View customer_data</a></li>
            </ul>
        </div>
    </header>

    <!-- Main Content Area -->
    <main class="main-wrapper">
        <div class="hero-header">
            <div class="hero-pill">
                <span>&#127758;</span> Plan Your Global Adventure
            </div>
            <h1>Traveler Registration</h1>
            <p>Complete your profile below to begin your adventure. Your data is securely synchronized to our AWS EC2 MySQL cluster.</p>
        </div>

        <div class="glass-card">
            <!-- Server-Side Error Alert -->
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger" role="alert">
                    <span style="font-size: 1.25rem;">&#9888;</span>
                    <div>
                        <strong>Registration Notice:</strong>
                        <p style="margin-top: 0.25rem;">${errorMessage}</p>
                    </div>
                </div>
            </c:if>

            <!-- Registration Form -->
            <form id="registrationForm" action="${pageContext.request.contextPath}/register" method="POST">
                <div class="form-grid">
                    
                    <!-- 1. Full Name -->
                    <div class="form-group full-width">
                        <label for="name" class="form-label">
                            Full Name <span class="required-star">*</span>
                        </label>
                        <input type="text" id="name" name="name" class="form-control" 
                               placeholder="e.g. Alexander Wright" 
                               value="${formName != null ? formName : ''}" 
                               required autocomplete="name" />
                        <span class="form-hint">Enter your legal full name as shown on your passport.</span>
                    </div>

                    <!-- 2. Age -->
                    <div class="form-group">
                        <label for="age" class="form-label">
                            Age (Years) <span class="required-star">*</span>
                        </label>
                        <input type="number" id="age" name="age" class="form-control" 
                               min="1" max="120" placeholder="e.g. 28" 
                               value="${formAge != null ? formAge : ''}" 
                               required />
                        <span class="form-hint">Must be between 1 and 120.</span>
                    </div>

                    <!-- 3. Gender -->
                    <div class="form-group">
                        <label for="gender" class="form-label">
                            Gender <span class="required-star">*</span>
                        </label>
                        <select id="gender" name="gender" class="form-control" required>
                            <option value="" disabled ${empty formGender ? 'selected' : ''}>Select Gender</option>
                            <option value="Male" ${formGender == 'Male' ? 'selected' : ''}>Male</option>
                            <option value="Female" ${formGender == 'Female' ? 'selected' : ''}>Female</option>
                            <option value="Non-Binary" ${formGender == 'Non-Binary' ? 'selected' : ''}>Non-Binary</option>
                            <option value="Prefer not to say" ${formGender == 'Prefer not to say' ? 'selected' : ''}>Prefer not to say</option>
                        </select>
                        <span class="form-hint">Required for airline &amp; border manifest.</span>
                    </div>

                    <!-- 4. BMI & Integrated Calculator -->
                    <div class="form-group full-width">
                        <label for="bmi" class="form-label">
                            Body Mass Index (BMI) <span class="required-star">*</span>
                        </label>
                        
                        <div class="bmi-calculator-box">
                            <div class="bmi-calc-header">
                                <span class="bmi-calc-title">Interactive Quick Calculator (Optional Helper)</span>
                                <span id="bmiBadge" class="bmi-badge-display badge-neutral">Enter BMI or use calc</span>
                            </div>
                            <div class="bmi-inputs-row">
                                <input type="number" id="heightInput" class="form-control" 
                                       placeholder="Height in cm (e.g. 175)" min="50" max="250" step="1" />
                                <input type="number" id="weightInput" class="form-control" 
                                       placeholder="Weight in kg (e.g. 70)" min="20" max="300" step="0.5" />
                                <div style="display: flex; align-items: center;">
                                    <input type="number" id="bmi" name="bmi" class="form-control" 
                                           step="0.01" min="10" max="90" 
                                           placeholder="BMI Value" 
                                           value="${formBmi != null ? formBmi : ''}" 
                                           required style="min-width: 130px; font-weight: 700;" />
                                </div>
                            </div>
                        </div>
                        <span class="form-hint">Type your BMI directly or fill Height &amp; Weight to calculate automatically.</span>
                    </div>

                    <!-- 5. Desired Country to Travel (MANDATORY TEXT FILL) -->
                    <div class="form-group full-width">
                        <label for="desiredCountry" class="form-label">
                            Desired Country to Travel <span class="required-star">*</span>
                        </label>
                        <input type="text" id="desiredCountry" name="desiredCountry" class="form-control" 
                               placeholder="e.g. Switzerland, Japan, Italy..." 
                               value="${formDesiredCountry != null ? formDesiredCountry : ''}" 
                               required />
                        <span class="form-hint">Mandatory destination text field. You can also pick from popular suggestions:</span>
                        
                        <div class="destination-chips">
                            <button type="button" class="chip-btn" data-country="Japan">&#127471;&#127477; Japan</button>
                            <button type="button" class="chip-btn" data-country="Switzerland">&#127464;&#127469; Switzerland</button>
                            <button type="button" class="chip-btn" data-country="Italy">&#127470;&#127481; Italy</button>
                            <button type="button" class="chip-btn" data-country="New Zealand">&#127475;&#127487; New Zealand</button>
                            <button type="button" class="chip-btn" data-country="Iceland">&#127470;&#127488; Iceland</button>
                            <button type="button" class="chip-btn" data-country="Canada">&#127464;&#127462; Canada</button>
                            <button type="button" class="chip-btn" data-country="Norway">&#127475;&#127476; Norway</button>
                        </div>
                    </div>

                </div>

                <!-- Submit Button -->
                <button type="submit" id="submitBtn" class="btn-submit">
                    <span>&#128640;</span> Submit &amp; Save to Database
                </button>
            </form>
        </div>
    </main>

    <!-- Footer -->
    <footer>
        <div class="footer-content">
            <div>
                <strong>Travel App</strong> &bull; Servlet 6.0 &bull; JSP &bull; Tomcat 10 &bull; MySQL on AWS EC2
            </div>
            <div>
                CI/CD Pipeline: Jenkins &bull; Target Table: <code>customer_data</code>
            </div>
        </div>
    </footer>

    <script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>

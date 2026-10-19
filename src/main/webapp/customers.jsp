<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Customer Data - Travel App</title>
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
                <li><a href="${pageContext.request.contextPath}/customers" class="nav-link active">Travelers List</a></li>
                <li><a href="${pageContext.request.contextPath}/index.jsp" class="nav-btn">+ New Traveler</a></li>
            </ul>
        </div>
    </header>

    <!-- Main Content -->
    <main class="main-wrapper">
        <div class="table-header-bar">
            <div>
                <h1 style="font-size: 1.85rem; font-weight: 800; color: #0f172a;">
                    Database Records: <code>customer_data</code>
                </h1>
                <p style="color: #64748b; font-size: 0.95rem; margin-top: 0.25rem;">
                    Live records persisted in the MySQL database running on AWS EC2.
                </p>
            </div>
            
            <div style="display: flex; align-items: center; gap: 1rem; flex-wrap: wrap;">
                <div class="search-box">
                    <span class="search-icon">&#128269;</span>
                    <input type="text" id="customerSearch" class="form-control" placeholder="Search by name, country...">
                </div>

                <span class="badge badge-info" style="font-size: 0.9rem; padding: 0.55rem 1rem;">
                    Total Travelers: <strong>${totalCount != null ? totalCount : 0}</strong>
                </span>
            </div>
        </div>

        <div class="glass-card" style="padding: 1rem;">
            <div class="table-responsive">
                <table id="customerTable" class="modern-table">
                    <thead>
                        <tr>
                            <th style="width: 70px;">ID</th>
                            <th>Traveler Name</th>
                            <th style="width: 80px;">Age</th>
                            <th style="width: 120px;">Gender</th>
                            <th style="width: 180px;">BMI &amp; Category</th>
                            <th>Desired Country</th>
                            <th style="width: 170px;">Registered At</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty customers}">
                                <c:forEach var="c" items="${customers}">
                                    <tr>
                                        <td><strong>#${c.id}</strong></td>
                                        <td>
                                            <div style="font-weight: 600; color: #1e293b;">${c.name}</div>
                                        </td>
                                        <td>${c.age}</td>
                                        <td><span class="badge badge-neutral">${c.gender}</span></td>
                                        <td>
                                            <span style="font-weight: 700; margin-right: 0.35rem;">${c.bmi}</span>
                                            <span class="badge ${c.bmiBadgeClass}">${c.bmiCategory}</span>
                                        </td>
                                        <td>
                                            <div style="font-weight: 600; color: #4f46e5;">
                                                &#127758; ${c.desiredCountry}
                                            </div>
                                        </td>
                                        <td style="color: #64748b; font-size: 0.85rem;">
                                            <c:choose>
                                                <c:when test="${not empty c.createdAt}">
                                                    ${c.createdAt}
                                                </c:when>
                                                <c:otherwise>Just now</c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="7">
                                        <div class="empty-state">
                                            <div class="empty-state-icon">&#127757;</div>
                                            <h3>No Travelers Registered Yet</h3>
                                            <p style="margin-top: 0.35rem;">Be the first adventurer to record a journey.</p>
                                            <div style="margin-top: 1.25rem;">
                                                <a href="${pageContext.request.contextPath}/index.jsp" class="btn-primary">
                                                    Register Traveler Now
                                                </a>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>

                <div id="searchEmptyMsg" class="empty-state" style="display: none;">
                    <div class="empty-state-icon">&#128269;</div>
                    <h3>No matching traveler found</h3>
                    <p>Try searching for a different name or destination country.</p>
                </div>
            </div>
        </div>
    </main>

    <!-- Footer -->
    <footer>
        <div class="footer-content">
            <div>
                <strong>Travel App</strong> &bull; Tomcat 10 &bull; MySQL on AWS EC2 &bull; Table: <code>customer_data</code>
            </div>
            <div>
                <a href="${pageContext.request.contextPath}/index.jsp" style="color: #4f46e5; text-decoration: none; font-weight: 600;">
                    &larr; Back to Registration
                </a>
            </div>
        </div>
    </footer>

    <script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>

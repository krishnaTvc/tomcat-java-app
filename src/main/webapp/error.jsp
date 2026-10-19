<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error - Travel App</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <header class="navbar">
        <div class="nav-container">
            <a href="${pageContext.request.contextPath}/index.jsp" class="brand">
                <div class="brand-icon">&#9992;</div>
                <span>Travel App</span>
            </a>
            <ul class="nav-links">
                <li><a href="${pageContext.request.contextPath}/index.jsp" class="nav-link">Home</a></li>
                <li><a href="${pageContext.request.contextPath}/customers" class="nav-link">Travelers List</a></li>
            </ul>
        </div>
    </header>

    <main class="main-wrapper">
        <div class="glass-card" style="max-width: 650px; margin: 0 auto; text-align: center;">
            <div style="font-size: 3.5rem; color: #ef4444; margin-bottom: 1rem;">&#9888;</div>
            <h1 style="font-size: 2rem; font-weight: 800; color: #0f172a; margin-bottom: 0.5rem;">An Error Occurred</h1>
            
            <p style="color: #64748b; margin-bottom: 1.5rem;">
                The application encountered an issue while processing your request.
            </p>

            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger" style="text-align: left;">
                    <div>
                        <strong>Details:</strong>
                        <div style="font-family: monospace; font-size: 0.85rem; margin-top: 0.25rem; word-break: break-all;">
                            ${errorMessage}
                        </div>
                    </div>
                </div>
            </c:if>

            <div style="background: #f8fafc; border: 1px solid #e2e8f0; border-radius: 8px; padding: 1.25rem; text-align: left; margin: 1.5rem 0; font-size: 0.9rem;">
                <strong>AWS EC2 Troubleshooting Checklist:</strong>
                <ul style="margin-left: 1.25rem; margin-top: 0.5rem; color: #475569; display: flex; flex-direction: column; gap: 0.35rem;">
                    <li>Ensure the MySQL EC2 Security Group allows inbound traffic on port <strong>3306</strong> from the Tomcat EC2 Private IP.</li>
                    <li>Verify MySQL bind-address is set to <code>0.0.0.0</code> in <code>/etc/mysql/mysql.conf.d/mysqld.cnf</code>.</li>
                    <li>Verify environment variables <code>DB_HOST</code>, <code>DB_USER</code>, and <code>DB_PASSWORD</code> in Tomcat's <code>setenv.sh</code> or <code>db.properties</code>.</li>
                </ul>
            </div>

            <div class="ticket-actions">
                <a href="${pageContext.request.contextPath}/index.jsp" class="btn-primary">
                    Return to Registration Form
                </a>
            </div>
        </div>
    </main>

    <footer>
        <div class="footer-content">
            <div><strong>Travel App</strong> &bull; Error Handler</div>
        </div>
    </footer>

</body>
</html>

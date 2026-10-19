pipeline {
    // 3. Dedicated Agent Selection (avoid unrestricted 'agent any')
    agent {
        label "${params.JENKINS_AGENT_LABEL ?: 'linux-maven-agent'}"
    }

    parameters {
        string(name: 'JENKINS_AGENT_LABEL', defaultValue: 'linux-maven-agent', description: 'Label of dedicated Jenkins build agent with JDK 17 & Maven installed')
        string(name: 'TOMCAT_WEBAPPS_DIR', defaultValue: '/opt/tomcat10/webapps', description: 'Target Tomcat webapps directory: /opt/tomcat10/webapps (manual install) or /var/lib/tomcat10/webapps (Ubuntu package)')
    }

    tools {
        maven 'Maven-3.9'
        jdk 'JDK-17'
    }

    environment {
        // AWS EC2 Target Deployment Configuration
        TOMCAT_EC2_HOST = credentials('TOMCAT_EC2_HOST') // Public or Private IP of Tomcat EC2 instance
        TOMCAT_SSH_USER = 'ubuntu'                       // Default AMI user ('ubuntu' or 'ec2-user')
        APP_NAME = 'travel-app'
        WAR_FILE = "target/${APP_NAME}.war"

        // 4. Tomcat path resolution (supports manual tarball or Ubuntu apt package)
        TOMCAT_WEBAPPS_DIR = "${params.TOMCAT_WEBAPPS_DIR ?: '/opt/tomcat10/webapps'}"

        // Jenkins SSH Credentials ID (configured in Manage Jenkins -> Credentials)
        SSH_CREDENTIALS_ID = 'aws-ec2-ssh-key'
    }

    options {
        timeout(time: 30, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timestamps()
    }

    stages {
        stage('Checkout Source Code') {
            steps {
                echo 'Checking out source code from Git repository...'
                checkout scm
            }
        }

        stage('Build & Test with Maven') {
            steps {
                echo 'Building Travel App WAR, running unit tests and integration tests...'
                // 7. Verify phase runs both unit tests and integration tests (*IT.java)
                sh 'mvn clean verify -DskipTests=false'
            }
            post {
                always {
                    junit testResults: '**/target/surefire-reports/*.xml,**/target/failsafe-reports/*.xml', allowEmptyResults: true
                }
            }
        }

        stage('Archive Artifacts') {
            steps {
                echo "Archiving ${WAR_FILE} artifact..."
                archiveArtifacts artifacts: 'target/*.war', fingerprint: true
            }
        }

        stage('Deploy to Tomcat 10 on AWS EC2') {
            steps {
                echo "Deploying ${WAR_FILE} to AWS EC2 Tomcat at ${TOMCAT_EC2_HOST}..."
                
                // 8. SSH verification: scan host key into known_hosts instead of StrictHostKeyChecking=no
                sshagent([SSH_CREDENTIALS_ID]) {
                    sh """
                        echo "Verifying and registering EC2 SSH host key..."
                        mkdir -p ~/.ssh && chmod 700 ~/.ssh
                        ssh-keyscan -H "${TOMCAT_EC2_HOST}" >> ~/.ssh/known_hosts 2>/dev/null
                        chmod 644 ~/.ssh/known_hosts

                        echo "Copying WAR file to EC2 /tmp directory..."
                        scp ${WAR_FILE} ${TOMCAT_SSH_USER}@${TOMCAT_EC2_HOST}:/tmp/${APP_NAME}.war
                        
                        echo "1. Deploying consistently as ${APP_NAME}.war to ${TOMCAT_WEBAPPS_DIR}..."
                        ssh ${TOMCAT_SSH_USER}@${TOMCAT_EC2_HOST} '
                            sudo cp /tmp/${APP_NAME}.war ${TOMCAT_WEBAPPS_DIR}/${APP_NAME}.war
                            sudo chown tomcat:tomcat ${TOMCAT_WEBAPPS_DIR}/${APP_NAME}.war 2>/dev/null || \
                            sudo chown tomcat10:tomcat10 ${TOMCAT_WEBAPPS_DIR}/${APP_NAME}.war 2>/dev/null || true
                            echo "Application deployed as ${APP_NAME}.war into ${TOMCAT_WEBAPPS_DIR}"
                        '
                    """
                }
            }
        }

        stage('Smoke Test & Health Check') {
            steps {
                echo "2. Validating deployed application endpoint at http://${TOMCAT_EC2_HOST}:8080/${APP_NAME}/index.jsp..."
                // 2. Strict smoke test: curl --fail will exit non-zero on HTTP errors (e.g. 404, 500)
                sh """
                    echo "Polling application endpoint for HTTP 200 OK..."
                    sleep 5
                    curl --fail --silent --show-error \
                         --retry 12 \
                         --retry-delay 5 \
                         --retry-connrefused \
                         "http://${TOMCAT_EC2_HOST}:8080/${APP_NAME}/index.jsp" > /dev/null
                    echo "Smoke test passed successfully: Application is active and healthy!"
                """
            }
        }
    }

    post {
        success {
            echo "========================================================"
            echo "SUCCESS: Travel App built and deployed to Tomcat 10 EC2!"
            echo "Access your app at: http://${TOMCAT_EC2_HOST}:8080/${APP_NAME}/"
            echo "========================================================"
        }
        failure {
            echo "========================================================"
            echo "FAILURE: Pipeline execution failed. Inspect logs for details."
            echo "========================================================"
        }
        cleanup {
            cleanWs notFailBuild: true
        }
    }
}

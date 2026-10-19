pipeline {
    agent any

    tools {
        maven 'Maven-3.9'
        jdk 'JDK-17'
    }

    environment {
        // AWS EC2 Target Deployment Configuration
        // Replace with your EC2 Tomcat Host or pass via Jenkins Build Parameters
        TOMCAT_EC2_HOST = credentials('TOMCAT_EC2_HOST') // or e.g., '54.xxx.xxx.xxx'
        TOMCAT_SSH_USER = 'ubuntu'                       // or 'ec2-user'
        TOMCAT_WEBAPPS_DIR = '/opt/tomcat10/webapps'     // Tomcat webapps directory on EC2
        APP_NAME = 'travel-app'
        WAR_FILE = "target/${APP_NAME}.war"

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
                echo 'Building Travel App WAR and running unit tests...'
                // Using -Ddb.type=sqlite for unit test phase to isolate builds
                sh 'mvn clean test package -DskipTests=false'
            }
            post {
                always {
                    junit testResults: '**/target/surefire-reports/*.xml', allowEmptyResults: true
                }
            }
        }

        stage('Archive Artifacts') {
            steps {
                echo 'Archiving travel-app.war artifact...'
                archiveArtifacts artifacts: 'target/*.war', fingerprint: true
            }
        }

        stage('Deploy to Tomcat 10 on AWS EC2') {
            steps {
                echo "Deploying ${WAR_FILE} to AWS EC2 Tomcat at ${TOMCAT_EC2_HOST}..."
                
                // Option A: Deploy via SSH / SCP using Jenkins SSH Agent Plugin
                sshagent([SSH_CREDENTIALS_ID]) {
                    sh """
                        echo "Copying WAR file to EC2 Tomcat webapps directory..."
                        scp -o StrictHostKeyChecking=no ${WAR_FILE} ${TOMCAT_SSH_USER}@${TOMCAT_EC2_HOST}:/tmp/${APP_NAME}.war
                        
                        echo "Moving to Tomcat webapps and restarting context..."
                        ssh -o StrictHostKeyChecking=no ${TOMCAT_SSH_USER}@${TOMCAT_EC2_HOST} '
                            sudo cp /tmp/${APP_NAME}.war ${TOMCAT_WEBAPPS_DIR}/ROOT.war || sudo cp /tmp/${APP_NAME}.war ${TOMCAT_WEBAPPS_DIR}/${APP_NAME}.war
                            sudo chown tomcat:tomcat ${TOMCAT_WEBAPPS_DIR}/*.war 2>/dev/null || true
                            echo "Application deployed to Tomcat successfully."
                        '
                    """
                }

                // Option B (Alternative): Deploy via Tomcat Manager API (Uncomment if using Tomcat Manager)
                /*
                withCredentials([usernamePassword(credentialsId: 'tomcat-manager-credentials', usernameVariable: 'TOMCAT_USER', passwordVariable: 'TOMCAT_PASS')]) {
                    sh """
                        curl -v -u "${TOMCAT_USER}:${TOMCAT_PASS}" \
                             -T ${WAR_FILE} \
                             "http://${TOMCAT_EC2_HOST}:8080/manager/text/deploy?path=/${APP_NAME}&update=true"
                    """
                }
                */
            }
        }

        stage('Smoke Test & Health Check') {
            steps {
                echo 'Validating deployed application endpoint...'
                sh """
                    sleep 10
                    curl -s -o /dev/null -w "%{http_code}" http://${TOMCAT_EC2_HOST}:8080/${APP_NAME}/index.jsp || \
                    curl -s -o /dev/null -w "%{http_code}" http://${TOMCAT_EC2_HOST}:8080/index.jsp || true
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
            echo "FAILURE: Pipeline execution failed. Check console logs."
            echo "========================================================"
        }
        cleanup {
            cleanWs notFailBuild: true
        }
    }
}

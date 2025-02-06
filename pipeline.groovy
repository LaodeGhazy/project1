pipeline {
    agent any
    environment {
        CONTAINER_NAME = "my-web-app" 
    }
    stages {
        stage('Checkout Code') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[credentialsId: 'server_aplikasi_ssh', url: 'https://github.com/LaodeGhazy/project1.git']]]) 
        }
        stage('Periksa Status Aplikasi') {
            steps {
                sh 'check_app_status.sh' 
            }
            post {
                failure {
                    steps {
                        sshagent(credentials: ['server_aplikasi_ssh']) { 
                            sh "docker restart ${my-web-app}"
                        }
                        // Email notifikasi (opsional)
                        // email {
                        //     to: 'your_email@example.com'
                        //     subject: "Restart Service Gagal"
                        //     body: "Restart service untuk ${CONTAINER_NAME} gagal."
                        // }
                    }
                }
                success {
                    steps {
                        // Email notifikasi (opsional)
                        // email {
                        //     to: 'your_email@example.com'
                        //     subject: "Restart Service Sukses"
                        //     body: "Restart service untuk ${CONTAINER_NAME} sukses."
                        // }
                    }
                }
            }
        }
    }
}

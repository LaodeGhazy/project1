pipeline {
    agent any
    environment {
        CONTAINER_NAME = "my-web-app" // Ganti dengan nama container Anda
    }
    stages {
        stage('Checkout Code') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[credentialsId: 'laode_ghazy', url: 'https://github.com/LaodeGhazy/project1.git']]]) // Periksa ID kredensial dan URL repository
            }
        }
        stage('Periksa Status Aplikasi') {
            steps {
                sh 'check_app_status.sh'  // Pastikan path ini benar!
            }
            post {
                failure {
                    steps {
                        sshagent(credentials: ['server_aplikasi_ssh']) { // Periksa ID kredensial SSH
                            sh "docker restart ${CONTAINER_NAME}"
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

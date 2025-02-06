pipeline {
    agent any
    environment {
        CONTAINER_NAME = "my-web-app" // Ganti dengan nama container Anda
    }
    stages {
        stage('Checkout Code') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[credentialsId: 'laode_ghazy', url: 'https://github.com/LaodeGhazy/project1.git']]])
            }
        }
        stage('Periksa Status Aplikasi') {
            steps {
                sh 'scripts/check_app_status.sh' // Pastikan path ini benar
            }
            post {
                failure {
                    steps {
                        // Restart service langsung di sini, tanpa stage lagi
                        sshagent(credentials: ['server_aplikasi_ssh']) {
                            sh "docker restart ${CONTAINER_NAME}"
                        }
                        // Notifikasi email (opsional)
                        // email {
                        //     to: 'your_email@example.com'
                        //     subject: "Restart Service Gagal"
                        //     body: "Restart service untuk ${CONTAINER_NAME} gagal."
                        // }
                    }
                }
                success {
                    steps {
                        // Notifikasi email (opsional)
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

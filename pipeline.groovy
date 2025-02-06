pipeline {
    agent any
    environment {
        DOCKER_IMAGE = "my-web-app" // Ganti dengan nama image Docker Anda
        CONTAINER_NAME = "my-web-app" // Ganti dengan nama container Docker Anda
    }
    stages {
        stage('Checkout Code') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[credentialsId: 'laode_ghazy', url: 'https://github.com/LaodeGhazy/project1.git']]]) // Ganti dengan info Git Anda
            }
        }
        stage('Periksa Status Aplikasi') {
            steps {
                sh 'check_app_status.sh' // Pastikan path ini benar! (relatif setelah checkout)
            }
            post {
                failure {
                    steps {
                        stage('Restart Service') {
                            steps {
                                sshagent(credentials: ['server_aplikasi_ssh']) {
                                    sh "docker restart ${04fcd613ac0b}" // Gunakan variabel environment
                                }
                            }
                        }
                        // Opsi: Tambahkan notifikasi email di sini jika restart gagal
                        // email {
                        //     to: 'your_email@example.com'
                        //     subject: "Restart Service Gagal"
                        //     body: "Restart service untuk ${CONTAINER_NAME} gagal."
                        // }
                    }
                }
                success {
                   // Opsi: Tambahkan notifikasi email di sini jika restart sukses
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

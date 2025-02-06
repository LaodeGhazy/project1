pipeline {
    agent any
    environment {
        DOCKER_IMAGE = "my-web-app" // Ganti dengan nama image Docker Anda (jika perlu)
        CONTAINER_NAME = "my-web-app" // Ganti dengan nama container Docker Anda (jika perlu)
    }
    stages {
        stage('Checkout Code') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[credentialsId: 'laode_ghazy', url: 'https://github.com/LaodeGhazy/project1.git']]])
            }
        }
        stage('Periksa Status Aplikasi') {
            steps {
                sh 'scripts/check_app_status.sh' // Pastikan path ini benar! (relatif setelah checkout)
            }
            post {
                failure {
                    steps {
                        stage('Restart Service') {
                            steps {
                                sshagent(credentials: ['server_aplikasi_ssh']) {
                                    // Gunakan CONTAINER_NAME dari environment, bukan hardcoded ID container
                                    sh "docker restart ${CONTAINER_NAME}" 
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
                    steps {
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
}

import java.util.Properties

plugins {
    id("dronesecurity-library")
}

tasks {

    register("createCertProperties") {
        doFirst {
            if (System.getenv("CI") == true.toString()) {
                val droneCertFileName = "Drone.cert.pem"
                val privateKeyFileName = "Drone.private.key.pem"
                val rootCAFileName = "root-CA.pem"
                val certFolderPath = mkdir("certs")
                val droneCert = file(certFolderPath.path + File.separator + droneCertFileName)
                val privateKey = file(certFolderPath.path + File.separator + privateKeyFileName)
                val rootCA = file(certFolderPath.path + File.separator + rootCAFileName)
                val username = if (certFolderPath.path.contains(File.separator + "user-application" + File.separator)) {
                    "DroneSecurity: User"
                } else {
                    "DroneSecurity: Drone"
                }

                droneCert.appendText(System.getenv("DRONE_CERT"))
                privateKey.appendText(System.getenv("PRIVATE_KEY"))
                rootCA.appendText(System.getenv("ROOT_CA"))
                val endpoint = System.getenv("ENDPOINT")

                val projectProperties = Properties()
                projectProperties.setProperty("certsFolderPath", certFolderPath.path + File.separator)
                projectProperties.setProperty("certificateFile", droneCertFileName)
                projectProperties.setProperty("privateKeyFile", privateKeyFileName)
                projectProperties.setProperty("certificateAuthorityFile", rootCAFileName)
                projectProperties.setProperty("endpoint", endpoint)
                projectProperties.setProperty("clientID", username)
                projectProperties.store(file(projectDir.path + File.separator + "project.properties").outputStream(), "Connection settings")
            }
        }
    }

    register("clearCerts") {
        doLast {
            if (System.getenv("CI") == true.toString()) {
                val certFolderPath = file(projectDir.path + File.separator + "certs")

                file(projectDir.path + File.separator + "project.properties").delete()
                certFolderPath.deleteRecursively()
            }
        }
    }


    test {
        dependsOn("createCertProperties")
        finalizedBy("clearCerts")
    }
}

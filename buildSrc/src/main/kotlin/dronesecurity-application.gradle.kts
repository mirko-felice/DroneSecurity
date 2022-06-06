import java.net.HttpURLConnection

plugins {
    id("dronesecurity-library")
    application
}

application {
    afterEvaluate {
        mainModule.set(project.extra["mainModuleName"].toString())
        mainClass.set(project.extra["mainClassName"].toString())
    }
}

tasks {

    distZip {
        enabled = false
    }

    distTar {
        enabled = false
    }

    startScripts {
        enabled = false
    }

    jar {
        enabled = false
    }

    javadoc {
        val libUrl = "https://javadoc.io/doc/io.github.dronesecurity.lib/latest"
        val connection = uri(libUrl).toURL().openConnection() as HttpURLConnection
        connection.instanceFollowRedirects = false
        if (connection.responseCode == 200)
                (options as StandardJavadocDocletOptions).links?.add(libUrl)
    }
}

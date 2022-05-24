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

    javadoc {
        (options as StandardJavadocDocletOptions).linksOffline?.add(
            JavadocOfflineLink("https://javadoc.io/doc/io.github.dronesecurity.lib/${project.version}", "../lib/build/docs/javadoc"))
    }
}

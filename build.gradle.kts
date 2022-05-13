import com.lordcodes.turtle.shellRun

plugins {
    id("org.sonarqube")
}

rootProject.version = shellRun {
    git.gitCommand(listOf("describe", "--tags"))
}

val organization: String by project
val githubUrl = "https://github.com/$organization/${rootProject.name}"

sonarqube.properties {
    property("sonar.organization", organization)
    property("sonar.host.url", "https://sonarcloud.io")
    property("sonar.python.version", 3.7)
    property("sonar.sourceEncoding", "UTF-8")
    property("sonar.projectName", rootProject.name)
    property("sonar.projectKey", rootProject.name)
    property("sonar.projectDescription", "System able to monitor the drone and manage the security thanks to some particular sensors.")
    property("sonar.projectVersion", rootProject.version.toString())
    property("sonar.login", System.getenv()["SONAR_TOKEN"] ?: "" )
    property("sonar.scm.provider", "git")
    property("sonar.verbose", "true")
    property("sonar.links.homepage", githubUrl)
    property("sonar.links.ci", "$githubUrl/actions")
    property("sonar.links.scm", githubUrl)
    property("sonar.links.issue", "$githubUrl/issues")
}

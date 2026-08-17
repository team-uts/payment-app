import org.springframework.boot.gradle.tasks.bundling.BootJar
import task.flyway.FlywayManager

val jar: Jar by tasks
val bootJar: BootJar by tasks

bootJar.enabled = false
jar.enabled = true

plugins {
    alias(libs.plugins.flyway)
}

buildscript {
    dependencies {
        classpath(libs.flyway.mysql)
    }
}

dependencies {
    implementation(project(":domain"))

    implementation(libs.flyway.core)
    implementation(libs.flyway.mysql)
}

flyway {
    val flywayManager = FlywayManager.init()

    baselineDescription = "Start Flyway Migration!"
    baselineOnMigrate = true
    locations = flywayManager.getLocations()
    configFiles = flywayManager.getConfigFiles()
    user = flywayManager.getUser()
    password = flywayManager.getPassword()
}

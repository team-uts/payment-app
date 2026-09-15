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
    implementation(project(":shared"))

    implementation(libs.flyway.core)
    implementation(libs.flyway.mysql)

    implementation("org.springframework.boot:spring-boot-h2console")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.mysql:mysql-connector-j")
    testRuntimeOnly("com.h2database:h2")

    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
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

import extensions.pluginId

plugins {
    java
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependencyManagement)
}

val projectLibs = rootProject.libs

allprojects {
    group = "dev.teamuts.payment"
    description = "payment-app"
}

subprojects {
    apply {
        plugin("java")
        plugin(projectLibs.plugins.spring.boot.pluginId())
        plugin(projectLibs.plugins.spring.dependencyManagement.pluginId())
    }

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(projectLibs.versions.java.get())
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    dependencies {
        implementation("org.springframework.boot:spring-boot-h2console")
        implementation("org.springframework.boot:spring-boot-starter-data-jpa")
        implementation("org.springframework.boot:spring-boot-starter-webmvc")
        compileOnly("org.projectlombok:lombok")
        runtimeOnly("com.h2database:h2")
        runtimeOnly("com.mysql:mysql-connector-j")
        annotationProcessor("org.projectlombok:lombok")
        testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
        testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
        testCompileOnly("org.projectlombok:lombok")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
        testAnnotationProcessor("org.projectlombok:lombok")
    }
}

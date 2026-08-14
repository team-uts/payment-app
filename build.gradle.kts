plugins {
    java
    id("org.springframework.boot") version "4.1.0"
    id("io.spring.dependency-management") version "1.1.7"
}

allprojects {
    group = "dev.teamuts.payment"
    version = "0.0.1-SNAPSHOT"
    description = "payment-app"
}

subprojects {
    apply {
        plugin("java")
        plugin("org.springframework.boot")
        plugin("io.spring.dependency-management")
    }

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
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

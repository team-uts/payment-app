import extensions.pluginId
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import task.BuildLifecyclePlugin
import task.test.TestContainer
import task.test.TestLoggingUtils
import task.test.TestSummary

plugins {
    java
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependencyManagement)
    alias(libs.plugins.spotless)
}

val projectLibs = rootProject.libs

allprojects {
    group = "dev.teamuts.payment"
    description = "payment-app"

    apply {
        plugin(projectLibs.plugins.spotless.pluginId())
    }
    spotless {
        java {
            googleJavaFormat()
            removeUnusedImports()
        }
    }
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
        implementation("org.springframework.boot:spring-boot-starter-webmvc")
        implementation("org.springframework:spring-tx")

        compileOnly("org.projectlombok:lombok")
        annotationProcessor("org.projectlombok:lombok")
        testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
        testCompileOnly("org.projectlombok:lombok")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
        testAnnotationProcessor("org.projectlombok:lombok")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        jvmArgs("-Xshare:off")  // Class Data Sharing(CDS) 비활성화

        testLogging {
            events = setOf(
                TestLogEvent.FAILED,
                TestLogEvent.SKIPPED,
                TestLogEvent.STANDARD_ERROR
            )

            exceptionFormat = TestExceptionFormat.FULL
            showExceptions = true
            showCauses = true
            showStackTraces = true
        }

//        ignoreFailures = true

        addTestListener(object : TestListener {
            override fun beforeSuite(desc: TestDescriptor) {}

            // handling after all test finished
            override fun afterSuite(desc: TestDescriptor, result: TestResult) {
                if (desc.parent != null) return

                val summary = TestSummary(
                    projectName = project.name,
                    taskName = name,
                    result = result
                )

                TestContainer.testResults = summary
            }

            override fun beforeTest(desc: TestDescriptor) {}

            // handling after each test finished
            override fun afterTest(desc: TestDescriptor, result: TestResult) {
                TestLoggingUtils.printEachResult(desc, result)
            }
        })
    }

    pluginManager.apply(BuildLifecyclePlugin::class.java)
}

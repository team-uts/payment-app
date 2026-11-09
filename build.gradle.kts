import dev.teamuts.gradle.utils.pluginId

plugins {
  java
  alias(libs.plugins.spring.boot)
  alias(libs.plugins.spring.dependencyManagement)
  alias(libs.plugins.spotless)
  alias(libs.plugins.teamuts.paymentConvention).apply(false)
}

val projectLibs = libs

allprojects {
  group = "dev.teamuts.payment"
  description = "payment-app"

  apply {
    plugin(projectLibs.plugins.spotless.pluginId())
  }

  spotless {
    java {
      target("src/**/*.java")
      targetExclude("**/generated/**")
      googleJavaFormat("2.0.0")
      removeUnusedImports()
    }

    kotlinGradle {
      target("**/*.gradle.kts")
      targetExclude("**/build/**")

      ktfmt().googleStyle()
      trimTrailingWhitespace()
      endWithNewline()
    }
  }
}

subprojects {
  apply {
    plugin("java")
    plugin(projectLibs.plugins.spring.boot.pluginId())
    plugin(projectLibs.plugins.spring.dependencyManagement.pluginId())
    plugin(projectLibs.plugins.teamuts.paymentConvention.pluginId())
  }

  java {
    toolchain {
      languageVersion = JavaLanguageVersion.of(projectLibs.versions.java.get())
    }
  }

  // retrieve mockito agent from payment-convention plugin
  val mockitoAgent = configurations.maybeCreate("mockitoAgent")
  dependencies {
    implementation(projectLibs.spring.boot.starter.webmvc)
    implementation(projectLibs.spring.tx)

    compileOnly(projectLibs.lombok)
    annotationProcessor(projectLibs.lombok)

    implementation(projectLibs.teamuts.payment.shared)

    testImplementation(projectLibs.spring.boot.starter.webmvc.test)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testCompileOnly(projectLibs.lombok)
    testAnnotationProcessor(projectLibs.lombok)
    testImplementation(projectLibs.mockito)
    mockitoAgent(projectLibs.mockito) { isTransitive = false }
  }

  tasks.withType<Test> {
    useJUnitPlatform()
  }
}

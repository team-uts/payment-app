@file:Suppress("UnstableApiUsage")

pluginManagement {
  repositories {
    // to use the payment-convention plugin
    maven { url = uri("https://team-uts.github.io/payment-gradle-plugins") }
    gradlePluginPortal()
    mavenCentral()
  }
}

dependencyResolutionManagement {
  // UnstableApiUsage
  // Do not allow individual modules or subprojects to define their own repositories
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    mavenCentral()
    // jitpack.io is used to fetch the payment-shared library
    maven { url = uri("https://jitpack.io") }
  }
}

rootProject.name = "payment-app"
include(
  "app",
  "domain",
  "persistence",
  "infra",
)

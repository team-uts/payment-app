@file:Suppress("UnstableApiUsage")

pluginManagement {
  repositories {
    // to use the payment-convention plugin
    maven { url = uri("https://team-uts.github.io/payment-gradle-plugins/maven-repo") }
    gradlePluginPortal()
    mavenCentral()
  }
}

dependencyResolutionManagement {
  // UnstableApiUsage
  // Do not allow individual modules or subprojects to define their own repositories
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    // payment shared modules.
    // The content filter is required, not an optimisation: this Pages site answers missing
    // paths with an intermittent 503 rather than 404, and Gradle aborts on 5xx instead of
    // falling through. Scoping the repo to the one group it serves means it is never asked
    // for anything else, so the order of these two entries stops mattering.
    maven {
      url = uri("https://team-uts.github.io/payment-shared/maven-repo")
      content { includeGroup("dev.teamuts.shared") }
    }
    mavenCentral()
  }
}

rootProject.name = "payment-app"

include(
  "app",
  "domain",
  "persistence",
  "infra",
)

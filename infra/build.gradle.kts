import org.springframework.boot.gradle.tasks.bundling.BootJar

val jar: Jar by tasks

val bootJar: BootJar by tasks

bootJar.enabled = false

jar.enabled = true

dependencies {
  implementation(project(":domain"))

  // payment gateway (stripe)
  implementation(libs.stripe.java)
  // event streaming (kafka)
  implementation(libs.spring.boot.starter.kafka)
}

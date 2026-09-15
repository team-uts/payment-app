import com.diffplug.gradle.spotless.SpotlessCheck

afterEvaluate {
    val multiModules = listOf(
        ":domain",
        ":shared",
        ":persistence",
        ":infra"
    )
    project.tasks.apply {
        // depends on sub-modules test tasks
        this.withType<Test> {
            dependsOn(multiModules.map { "$it:test" })
        }
        // spotless checking for all sub-modules
        this.withType<SpotlessCheck> {
            dependsOn(multiModules.map { "$it:spotlessCheck" })
        }
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":shared"))
    implementation(project(":infra"))
    implementation(project(":persistence"))
}
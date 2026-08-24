afterEvaluate {
    project.tasks.apply {
        // depends on sub-modules test tasks
        this.withType<Test> {
            dependsOn(
                listOf(
                    ":domain",
                    ":shared",
                    ":persistence",
                    ":infra"
                ).map { "$it:test" }
            )
        }
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":shared"))
    implementation(project(":infra"))
    implementation(project(":persistence"))
}
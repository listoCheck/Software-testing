plugins {
    id("java")
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")
    testCompileOnly("org.projectlombok:lombok:1.18.34")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.34")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    // Run only unit tests by default (exclude integration-tagged tests).
    useJUnitPlatform {
        excludeTags("integration")
    }
}

fun registerIntegrationStageTask(
    taskName: String,
    taskDescription: String,
    includePattern: String
) = tasks.register<Test>(taskName) {
    description = taskDescription
    group = "verification"
    dependsOn(tasks.testClasses)
    testClassesDirs = sourceSets.test.get().output.classesDirs
    classpath = sourceSets.test.get().runtimeClasspath
    include(includePattern)
    useJUnitPlatform {
        includeTags("integration")
    }
    shouldRunAfter(tasks.test)
}

val integrationMocksTest = registerIntegrationStageTask(
    taskName = "integrationMocksTest",
    taskDescription = "Runs stage 1 integration tests (isolated modules on mocks).",
    includePattern = "**/org/example/integration/mocks/**"
)

val integrationProgressiveTest = registerIntegrationStageTask(
    taskName = "integrationProgressiveTest",
    taskDescription = "Runs stage 2 integration tests (gradual replacement of mocks with real dependencies).",
    includePattern = "**/org/example/integration/progressive/**"
)

val integrationRealValuesTest = registerIntegrationStageTask(
    taskName = "integrationRealValuesTest",
    taskDescription = "Runs stage 3 integration tests (fully integrated real values).",
    includePattern = "**/org/example/integration/realvalues/**"
)

integrationProgressiveTest.configure {
    shouldRunAfter(integrationMocksTest)
}

integrationRealValuesTest.configure {
    shouldRunAfter(integrationProgressiveTest)
}

tasks.register("integrationTest") {
    description = "Runs integration pipeline: mocks -> progressive replacement -> real values."
    group = "verification"
    dependsOn(integrationMocksTest, integrationProgressiveTest, integrationRealValuesTest)
}

application {
    mainClass.set("org.example.Main")
}

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
    testImplementation("org.seleniumhq.selenium:selenium-java:4.30.0")
}

tasks.test {
    // Run only unit tests by default (exclude integration and UI-tagged tests).
    useJUnitPlatform {
        excludeTags("integration")
        excludeTags("ui")
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
    taskDescription = "Runs stage 1 integration tests (mocks).",
    includePattern = "**/org/example/integration/mocks/**"
)

val integrationRealValuesTest = registerIntegrationStageTask(
    taskName = "integrationRealValuesTest",
    taskDescription = "Runs stage 2 integration tests (real values).",
    includePattern = "**/org/example/integration/realvalues/**"
)

val integrationSystemTest = registerIntegrationStageTask(
    taskName = "integrationSystemTest",
    taskDescription = "Runs stage 3 integration tests (full system).",
    includePattern = "**/org/example/integration/system/**"
)

integrationRealValuesTest.configure {
    shouldRunAfter(integrationMocksTest)
}

integrationSystemTest.configure {
    shouldRunAfter(integrationRealValuesTest)
}

tasks.register("integrationTest") {
    description = "Runs integration pipeline: mocks -> real values -> system."
    group = "verification"
    dependsOn(integrationMocksTest, integrationRealValuesTest, integrationSystemTest)
}

fun registerUiTask(taskName: String, browserName: String) = tasks.register<Test>(taskName) {
    description = "Runs UI tests in $browserName."
    group = "verification"
    dependsOn(tasks.testClasses)
    testClassesDirs = sourceSets.test.get().output.classesDirs
    classpath = sourceSets.test.get().runtimeClasspath
    systemProperty("browser", browserName)
    systemProperty("headless", System.getProperty("headless", "true"))
    useJUnitPlatform {
        includeTags("ui")
    }
}

registerUiTask("uiChromeTest", "chrome")
registerUiTask("uiFirefoxTest", "firefox")

tasks.register("uiTest") {
    description = "Runs UI tests in Chrome and Firefox."
    group = "verification"
    dependsOn("uiChromeTest", "uiFirefoxTest")
}

application {
    mainClass.set("org.example.Main")
}

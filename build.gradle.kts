plugins {
    id("java")
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")
    testCompileOnly("org.projectlombok:lombok:1.18.34")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.34")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-core:5.12.0")
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

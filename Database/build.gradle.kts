import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.21"
    id("io.ktor.plugin") version "2.3.0"
    kotlin("plugin.serialization") version "1.8.21"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:2.3.0")
    implementation("io.ktor:ktor-server-swagger:2.3.0")
    implementation("top.jfunc.json:Json-Gson:1.0")
    implementation("com.google.code.gson:gson:2.10.1")
    testImplementation(kotlin("test"))
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.ktor:ktor-server-core:2.3.0")
    implementation("io.ktor:ktor-server-netty:2.3.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
//    implementation("io.ktor:ktor-swagger-ui:1.6.3")
//    implementation("io.ktor:ktor-openapi:1.6.3")
    implementation("io.ktor:ktor-server-swagger:2.3.0")
    implementation("io.ktor:ktor-server-test-host:2.3.0")
    // mockk
    testImplementation("io.mockk:mockk:1.12.0")
    implementation("org.postgresql:postgresql:42.2.27")
    implementation("com.squareup.okhttp3:okhttp:4.9.1")

}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("MainKt")
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
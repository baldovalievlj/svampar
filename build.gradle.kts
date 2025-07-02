import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val exposed_version: String by project
val postgresql_version: String by project
val hikari_version: String by project
val koin_version: String by project

plugins {
    kotlin("jvm") version "1.8.10"
    id("io.ktor.plugin") version "2.2.3"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.10"
}

group = "com.example"
version = "0.0.1"
application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("ch.qos.logback:logback-classic:$logback_version") //1.2.11
//    implementation("org.slf4j:slf4j-api:2.0.5")
    implementation("io.ktor:ktor-server-core-jvm:2.2.4")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:2.2.4")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:2.2.4")
    implementation("io.ktor:ktor-serialization-jackson:$ktor_version")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.14.2")
    implementation("io.ktor:ktor-server-auth:2.2.4")
    implementation("io.ktor:ktor-server-auth-jwt:2.2.4")
    implementation("io.ktor:ktor-server-netty-jvm:2.2.4")
    implementation("io.ktor:ktor-server-auth-jvm:2.2.4")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-server-sessions-jvm:2.2.4")
    implementation("io.ktor:ktor-http:$ktor_version")
    implementation("com.github.librepdf:openpdf:1.3.29")
    implementation("org.xhtmlrenderer:flying-saucer-core:9.1.22")
    implementation("org.xhtmlrenderer:flying-saucer-pdf-openpdf:9.1.22")
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposed_version")
    implementation("org.postgresql:postgresql:$postgresql_version")
    implementation("com.zaxxer:HikariCP:$hikari_version")
    testImplementation("io.ktor:ktor-server-tests-jvm:2.2.4")
    implementation("org.mindrot:jbcrypt:0.4")
    implementation("org.flywaydb:flyway-core:9.21.0")
    implementation("io.insert-koin:koin-core:$koin_version")
    implementation("io.insert-koin:koin-ktor:$koin_version")
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version")
}
tasks {
    create("stage").dependsOn("installDist")
}

tasks.withType<ShadowJar> {
    archiveBaseName.set("http-api")
    archiveClassifier.set("all")
    archiveVersion.set("") // no version in filename
}
//tasks.register("stage") {
//    dependsOn("clean", "shadowJar")
//}
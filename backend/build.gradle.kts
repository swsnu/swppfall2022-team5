import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

import java.nio.file.Files
import java.nio.file.Path
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.ContentType
import org.apache.http.entity.mime.MultipartEntityBuilder
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

// Reference: https://github.com/nbaztec/coveralls-jacoco-gradle-plugin
class CustomCoverallsJacocoPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.task("customCoverallsJacoco") {
            description = "Reports coverage to coveralls with service_number"
            doLast {
                val serviceNumber = System.getenv("GITHUB_RUN_ID")
                val path = Path.of("./build/req.json")
                var content = Files.readString(path, Charsets.UTF_8)
                content = "${content.substring(0, content.length - 1)}, \"service_number\": $serviceNumber}"
                send(content)
            }
        }
    }

    private fun send(reqJson: String) {
        val endpoint = "https://coveralls.io/api/v1/jobs"
        val defaultHttpTimeoutMs = 10 * 1000

        val httpClient = HttpClients.createDefault()
        val httpPost = HttpPost(endpoint).apply {
            config = RequestConfig
                .custom()
                .setConnectTimeout(defaultHttpTimeoutMs)
                .setSocketTimeout(defaultHttpTimeoutMs)
                .setConnectionRequestTimeout(defaultHttpTimeoutMs)
                .build()

            entity = MultipartEntityBuilder
                .create()
                .addBinaryBody(
                    "json_file",
                    reqJson.toByteArray(Charsets.UTF_8),
                    ContentType.APPLICATION_JSON,
                    "json_file"
                )
                .build()
        }

        val res = httpClient.execute(httpPost)
        if (res.statusLine.statusCode != 200) {
            throw Exception(
                "coveralls returned HTTP ${res.statusLine.statusCode}: ${
                EntityUtils.toString(res.entity).trim()
                }"
            )
        }
    }
}

apply<CustomCoverallsJacocoPlugin>()

plugins {
    id("org.springframework.boot") version "2.7.5"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    id("org.jlleitschuh.gradle.ktlint") version "11.0.0"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
    kotlin("plugin.jpa") version "1.6.21"
    jacoco
    id("com.github.nbaztec.coveralls-jacoco") version "1.2.15"
    kotlin("kapt") version "1.7.10"
    kotlin("plugin.allopen") version "1.6.21"
}

group = "com.swpp"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.cloud:spring-cloud-starter-aws:2.2.6.RELEASE")
    runtimeOnly("com.mysql:mysql-connector-j")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("com.h2database:h2")
    implementation("com.drewnoakes:metadata-extractor:2.18.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("io.mockk:mockk:1.13.2")
    testImplementation("io.kotest:kotest-runner-junit5:5.5.4")
    testImplementation("io.kotest:kotest-assertions-core:5.5.4")
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
    implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")
    implementation("com.querydsl:querydsl-jpa:5.0.0")
    annotationProcessor(group = "com.querydsl", name = "querydsl-apt", classifier = "jpa")
    kapt("com.querydsl:querydsl-apt:5.0.0:jpa")
    api("com.querydsl:querydsl-jpa:5.0.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    systemProperty("spring.profiles.active", "test")
    useJUnitPlatform()
}

jacoco {
    toolVersion = "0.8.8"
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
        csv.required.set(false)
        html.required.set(true)
    }

    classDirectories.setFrom(
        files(
            classDirectories.files.map {
                fileTree(it) {
                    exclude(
                        "com/swpp/footprinter/test/**",
                    )
                }
            }
        )
    )
}

coverallsJacoco {
    dryRun = true
    coverallsRequest = File("build/req.json")
}

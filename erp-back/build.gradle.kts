import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    kotlin("plugin.jpa") version "1.9.25"
    id("org.springframework.boot") version "3.3.3"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "com.orion"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    val jakarta = "3.1.0"
    val aerogear = "1.0.0"
    val jjwtVersion = "0.12.6"
    val opencsv = "5.9"
    val commonsIo = "2.16.1"
    val poi = "5.3.0"
    val mockitoKt = "5.4.0"
    val instancio = "5.0.1"
    val querydsl = "5.1.0"

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jboss.aerogear:aerogear-otp-java:$aerogear")
    implementation("jakarta.validation:jakarta.validation-api:$jakarta")
    implementation("io.jsonwebtoken:jjwt-api:$jjwtVersion")
    implementation("com.opencsv:opencsv:$opencsv")
    implementation("commons-io:commons-io:$commonsIo")
    implementation("org.apache.poi:poi:$poi")
    implementation("org.apache.poi:poi-ooxml:$poi")
    implementation("com.querydsl:querydsl-jpa:$querydsl:jakarta")
    implementation("com.querydsl:querydsl-apt:$querydsl:jakarta")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("com.querydsl:querydsl-apt:$querydsl:jakarta")
    annotationProcessor("jakarta.annotation:jakarta.annotation-api")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    runtimeOnly("com.h2database:h2")
    runtimeOnly("com.mysql:mysql-connector-j")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:$jjwtVersion")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:$jjwtVersion")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.mockito.kotlin:mockito-kotlin:$mockitoKt")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.instancio:instancio-junit:$instancio")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
        freeCompilerArgs.addAll("-Xjsr305=strict")
        languageVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_1_9)
        apiVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_1_9)
    }
    jvmToolchain(JvmTarget.JVM_17.target.toInt())
}

//configurations.implementation {
//	exclude(module = "spring-boot-starter-logging")
//}

tasks.withType<Test> {
    useJUnitPlatform()
}

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    java
    id("org.springframework.boot") version "2.3.4.RELEASE"
    kotlin("jvm") version "1.4.20"
}

apply(plugin = "io.spring.dependency-management")

group = "org.qamock.webapp"
version = "1.0-SNAPSHOT"
description = "qamock-service"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenLocal()
    mavenCentral()
}

val springBootVersion by extra("2.3.4.RELEASE")
val springVersion by extra("5.2.11.RELEASE")
val springSecurityVersion by extra("5.3.5.RELEASE")
val hibernateVersion by extra("5.0.12.Final")
val sl4jVersion by extra("1.7.30")
val jacksonVersion by extra("2.12.0")

dependencies {
//    implementation("org.springframework.boot:spring-boot-starter:${springBootVersion}")
    implementation("org.springframework.boot:spring-boot-starter-web:${springBootVersion}")
    implementation("org.springframework.boot:spring-boot-starter-actuator:${springBootVersion}")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf:${springBootVersion}")
    implementation("org.springframework.boot:spring-boot-devtools:${springBootVersion}")
    implementation("org.springframework.boot:spring-boot-starter-security:${springBootVersion}")
    implementation("org.springframework:spring-oxm:${springVersion}")
    implementation("org.springframework:spring-jms:${springVersion}")
//    implementation("org.springframework:spring-context:${springVersion}")
    implementation("org.springframework:spring-webmvc:${springVersion}")
    implementation("org.springframework:spring-tx:${springVersion}")
    implementation("org.springframework:spring-orm:${springVersion}")
    implementation("org.springframework:spring-context-support:${springVersion}")
    implementation("org.springframework.boot:spring-boot-starter-amqp:${springBootVersion}")
    implementation("org.springframework.security:spring-security-web:${springSecurityVersion}")
    implementation("org.springframework.security:spring-security-config:${springSecurityVersion}")
    implementation("org.springframework.security:spring-security-core:${springSecurityVersion}")
    implementation("javax.validation:validation-api:1.1.0.Final")
    implementation("javax.mail:mail:1.4")
    implementation("javax.inject:javax.inject:1")
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("org.glassfish.mq:imq:5.1")
    implementation("com.fasterxml.jackson.core:jackson-core:${jacksonVersion}")
    implementation("com.fasterxml.jackson.core:jackson-databind:${jacksonVersion}")
    implementation("com.fasterxml.jackson.core:jackson-annotations:${jacksonVersion}")
    implementation("jakarta.xml.bind:jakarta.xml.bind-api:2.3.2")
    implementation("org.glassfish.jaxb:jaxb-runtime:2.3.2")
    implementation("org.hibernate:hibernate-core:${hibernateVersion}")
    implementation("org.hibernate:hibernate-entitymanager:${hibernateVersion}")
    implementation("org.apache.commons:commons-dbcp2:2.0")
    implementation("com.h2database:h2:1.4.200")
    implementation("org.codehaus.groovy:groovy-all:2.4.6")
    implementation("org.slf4j:slf4j-simple:${sl4jVersion}")
    implementation("org.slf4j:slf4j-api:${sl4jVersion}")
    testImplementation("org.springframework.boot:spring-boot-starter-test:${springBootVersion}")
    implementation("org.springframework.roo:org.springframework.roo.annotations:1.3.2.RELEASE")
    implementation(kotlin("stdlib-jdk8"))
}

configurations {
    configurations {
        all {
            exclude(module = "logback-classic")
        }
    }
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
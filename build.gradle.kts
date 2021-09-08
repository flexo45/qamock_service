plugins {
    java
//    kotlin("jvm") version "1.5.21" apply false
}

group = "org.qamock"
version = "1.0-SNAPSHOT"
description = "qamock-service-bundle"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
}

//val springBootVersion by extra("2.3.4.RELEASE")
//val springVersion by extra("5.2.11.RELEASE")
//val springSecurityVersion by extra("5.3.5.RELEASE")
//val hibernateVersion by extra("5.0.12.Final")
//val sl4jVersion by extra("1.7.30")
//val jacksonVersion by extra("2.12.0")

//dependencies {
//    implementation(kotlin("stdlib"))
//}
//
//configurations {
//    configurations {
//        all {
//            exclude(module = "logback-classic")
//        }
//    }
//}
//
//val compileKotlin: org.jetbrains.kotlin.gradle.tasks.KotlinCompile by tasks
//compileKotlin.kotlinOptions {
//    jvmTarget = "11"
//}
//val compileTestKotlin: org.jetbrains.kotlin.gradle.tasks.KotlinCompile by tasks
//compileTestKotlin.kotlinOptions {
//    jvmTarget = "11"
//}
//
//repositories {
//    mavenLocal()
//    mavenCentral()
//    jcenter()
//}
//
//tasks.withType<JavaCompile>() {
//    options.encoding = "UTF-8"
//}
//val compileKotlin: KotlinCompile by tasks
//compileKotlin.kotlinOptions {
//    jvmTarget = "1.8"
//}
//val compileTestKotlin: KotlinCompile by tasks
//compileTestKotlin.kotlinOptions {
//    jvmTarget = "1.8"
//}
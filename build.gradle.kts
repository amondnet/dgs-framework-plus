import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenCentral()
    }
}

group = "com.balancefriends.graphql.dgs"


plugins {
    `java-library`
    id("nebula.dependency-recommender") version "11.0.0"
    id("org.jmailen.kotlinter") version "3.6.0"
    kotlin("jvm") version Versions.KOTLIN_VERSION
    kotlin("kapt") version Versions.KOTLIN_VERSION
    idea
    eclipse
}


repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
}

allprojects {
    group = "com.balancefriends.graphql.dgs"
    repositories {
        mavenCentral()
    }
    apply(plugin = "nebula.dependency-recommender")
    dependencyRecommendations {
        mavenBom(mapOf("module" to "org.springframework:spring-framework-bom:${Versions.SPRING_VERSION}"))
        mavenBom(mapOf("module" to "org.springframework.boot:spring-boot-dependencies:${Versions.SPRING_BOOT_VERSION}"))
        mavenBom(mapOf("module" to "org.springframework.security:spring-security-bom:${Versions.SPRING_SECURITY_VERSION}"))
        mavenBom(mapOf("module" to "org.springframework.cloud:spring-cloud-dependencies:${Versions.SPRING_CLOUD_VERSION}"))
        mavenBom(mapOf("module" to "com.fasterxml.jackson:jackson-bom:${Versions.JACKSON_BOM}"))
        mavenBom(mapOf("module" to "org.jetbrains.kotlin:kotlin-bom:${Versions.KOTLIN_VERSION}"))
    }
}

subprojects {
    apply {
        plugin("java-library")
        plugin("kotlin")
        plugin("kotlin-kapt")
        plugin("org.jmailen.kotlinter")
    }

    /**
     * Remove once the following ticket is closed:
     *  Kotlin-JVM: runtimeOnlyDependenciesMetadata, implementationDependenciesMetadata should be marked with isCanBeResolved=false
     *  https://youtrack.jetbrains.com/issue/KT-34394
     */
    /*
    tasks.named("generateLock") {
        doFirst {
            project.configurations.filter { it.name.contains("DependenciesMetadata") }.forEach {
                it.isCanBeResolved = false
            }
        }
    }*/

    dependencies {
        // Apply the BOM to applicable subprojects.
        api(platform("com.netflix.graphql.dgs:graphql-dgs-platform:4.9.0"))

        // Speed up processing of AutoConfig's produced by Spring Boot
        annotationProcessor("org.springframework.boot:spring-boot-autoconfigure-processor")
        // Produce Config Metadata for properties used in Spring Boot
        annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
        // Speed up processing of AutoConfig's produced by Spring Boot for Kotlin
        kapt("org.springframework.boot:spring-boot-autoconfigure-processor:${Versions.SPRING_BOOT_VERSION}")
        // Produce Config Metadata for properties used in Spring Boot for Kotlin
        kapt("org.springframework.boot:spring-boot-configuration-processor:${Versions.SPRING_BOOT_VERSION}")

        testImplementation("org.springframework.boot:spring-boot-starter-test") {
            exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        }
        testImplementation("io.mockk:mockk:1.12.0")
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kapt {
        arguments {
            arg(
                "org.springframework.boot.configurationprocessor.additionalMetadataLocations",
                "$projectDir/src/main/resources"
            )
        }
    }

    tasks.withType<JavaCompile>().configureEach {
        options.compilerArgs + "-parameters"
    }

    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            freeCompilerArgs += "-Xjvm-default=enable"
            jvmTarget = "1.8"
        }
    }

    tasks {
        test {
            useJUnitPlatform()
        }
    }

/*
    kotlinter {
        indentSize = 4
        reporters = arrayOf("checkstyle", "plain")
        experimentalRules = false
        disabledRules = arrayOf("no-wildcard-imports")
    }*/
}
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.20"
    `java-library`
}

repositories {
    jcenter()
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

//val fatJar = task("fatJar", type = Jar::class) {
//    archiveBaseName.set("my-storage-provider-fat")
//    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
//    with(tasks["jar"] as CopySpec)
//}

//tasks {
//    "build" {
//        dependsOn(fatJar)
//    }
//}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    compileOnly("org.keycloak:keycloak-server-spi:12.0.1")
    compileOnly("org.keycloak:keycloak-core:12.0.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:adapter-rxjava2:2.9.0")
    implementation("com.squareup.retrofit2:converter-jackson:2.9.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.2.3")
    implementation("at.favre.lib:bcrypt:0.9.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.1")

    // Logging
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("io.github.microutils:kotlin-logging:1.6.25")
}

tasks.withType<Jar> {
    exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA", "META-INF/*.MF")
    archiveBaseName.set("my-storage-provider-fat")
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}
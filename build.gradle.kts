plugins {
    `java-library`
    alias(libs.plugins.teavm) // order matters?
}

group = "dev.cephx"
version = "${libs.versions.cfr.get()}-teavm-SNAPSHOT"
description = "A JavaScript port of the CFR decompiler."

repositories {
    mavenCentral()
}

dependencies {
    api(libs.cfr)
    compileOnly(libs.teavm.core)
}

java.toolchain {
    languageVersion = JavaLanguageVersion.of(21)
}

teavm.js {
    mainClass = "dev.cephx.cfr.Main"
    moduleType = org.teavm.gradle.api.JSModuleType.ES2015
    // obfuscated = false
    // optimization = org.teavm.gradle.api.OptimizationLevel.NONE
}

tasks {
    register<Copy>("copyDist") {
        group = "build"

        from(generateJavaScript)
        into("dist")
    }

    build {
        dependsOn("copyDist")
    }
}

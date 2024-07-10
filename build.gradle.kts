plugins {
    `java-library`
    alias(libs.plugins.teavm) // order matters?
}

val thisVersion = "0.1.0"

group = "dev.cephx"
version = "$thisVersion-${libs.versions.cfr.get()}"
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

        from("README.md", "LICENSE", "LICENSE-CFR", generateJavaScript, "cfr.d.ts")
        into("dist")

        doLast {
            file("dist/package.json").writeText(
                """
                    {
                      "name": "@run-slicer/cfr",
                      "version": "${project.version}",
                      "description": "A JavaScript port of the CFR decompiler (https://github.com/leibnitz27/cfr).",
                      "main": "cfr.js",
                      "types": "cfr.d.ts",
                      "keywords": [
                        "decompiler",
                        "java",
                        "decompilation",
                        "cfr"
                      ],
                      "author": "run-slicer",
                      "license": "MIT"
                    }
                """.trimIndent()
            )
        }
    }

    build {
        dependsOn("copyDist")
    }
}

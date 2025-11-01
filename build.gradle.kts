plugins {
    `java-library`
    alias(libs.plugins.teavm) // order matters?
}

val thisVersion = "0.1.5"

group = "run.slicer"
version = "$thisVersion-${libs.versions.cfr.get()}"
description = "A JavaScript port of the CFR decompiler."

repositories {
    mavenCentral()
    maven("https://teavm.org/maven/repository")
}

dependencies {
    api(libs.cfr)
    compileOnly(libs.teavm.core)
}

java.toolchain {
    languageVersion = JavaLanguageVersion.of(21)
}

teavm.wasmGC {
    mainClass = "run.slicer.cfr.Main"
    modularRuntime = true
    /*obfuscated = false
    optimization = org.teavm.gradle.api.OptimizationLevel.NONE
    disassembly = true*/
}

/*tasks.disasmWasmGC {
    html = false
}*/

tasks {
    register<Copy>("copyDist") {
        group = "build"

        from(
            "README.md", "LICENSE", "LICENSE-CFR", "cfr.js", "cfr.d.ts",
            generateWasmGC, copyWasmGCRuntime
        )
        into("dist")

        duplicatesStrategy = DuplicatesStrategy.INCLUDE

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

    clean {
        delete("dist")
    }
}

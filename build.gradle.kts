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

teavm.js {
    mainClass = "dev.cephx.cfr.Main"
    // obfuscated = false
}

tasks {
    register<Copy>("copyDist") {
        group = "build"

        from(generateJavaScript)
        into("dist")
        rename { name ->
            if (name == "cfr.js" && teavm.js.obfuscated.get()) {
                return@rename "cfr.min.js"
            }

            return@rename name
        }
    }

    build {
        dependsOn("copyDist")
    }
}

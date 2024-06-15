plugins {
    `java-library`
    alias(libs.plugins.blossom)
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

val kotlin.reflect.KClass<*>.bytes: ByteArray
    get() = (java.classLoader ?: ClassLoader.getSystemClassLoader())
        .getResourceAsStream(java.name.replace('.', '/') + ".class")!!
        .use(`java.io`.InputStream::readAllBytes)

val ByteArray.base64String: String
    get() = `java.util`.Base64.getEncoder().encodeToString(this)

sourceSets {
    main {
        blossom {
            javaSources {
                property("javaLangObject", Object::class.bytes.base64String)
                property("javaLangString", String::class.bytes.base64String)
                property("javaLangSystem", System::class.bytes.base64String)
            }
        }
    }
}

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

val Class<*>.bytes: ByteArray
    get() = (classLoader ?: ClassLoader.getSystemClassLoader())
        .getResourceAsStream(name.replace('.', '/') + ".class")!!
        .use(`java.io`.InputStream::readAllBytes)

val ByteArray.base64String: String
    get() = `java.util`.Base64.getEncoder().encodeToString(this)

fun net.kyori.blossom.SourceTemplateSet.classes(vararg klasses: kotlin.reflect.KClass<*>) {
    properties.put("classes", klasses.associate { klass ->
        klass.javaObjectType.name to klass.javaObjectType.bytes.base64String
    })
}

sourceSets {
    main {
        blossom {
            javaSources {
                classes(
                    Object::class, Record::class, Enum::class,
                    Byte::class, Short::class, Int::class, Long::class,
                    Float::class, Double::class, Boolean::class, Character::class,
                    Void::class, String::class
                )
            }
        }
    }
}

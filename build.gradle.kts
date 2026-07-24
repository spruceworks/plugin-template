plugins {
    java
    id("com.gradleup.shadow") version "9.6.1"
    id("xyz.jpenilla.run-paper") version "3.0.2"
}

group = "dev.spruceworks"
version = "0.1.0"
description = "Reusable Paper plugin template"

java {
    // Paper 26.x requires Java 25 (https://docs.papermc.io/paper/dev/project-setup/).
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc"
    }
}

dependencies {
    // Version format per https://docs.papermc.io/paper/dev/project-setup/ — resolves the latest 26.2 build.
    compileOnly("io.papermc.paper:paper-api:26.2.build.+")

    implementation("org.bstats:bstats-bukkit:3.1.0")
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name()
        val props = mapOf("version" to project.version.toString())
        inputs.properties(props)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }

    jar {
        // The unshaded jar has no bStats classes and is not a usable plugin — label it clearly.
        archiveClassifier.set("unshaded")
    }

    shadowJar {
        archiveClassifier.set("")
        // Relocate bStats so it cannot clash with other plugins that bundle it.
        relocate("org.bstats", "dev.spruceworks.template.libs.bstats")
    }

    assemble {
        dependsOn(shadowJar)
    }

    runServer {
        // Downloads this Paper version and boots a local test server with the plugin installed.
        minecraftVersion("26.2")
    }
}

plugins {
    kotlin("jvm") version "1.5.31"
    id("com.github.johnrengelman.shadow") version "6.1.0"
    java
}

group = "me.centauri07.quest"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
    maven { url = uri ("https://oss.sonatype.org/content/groups/public/") }
}

dependencies {
    implementation("com.google.code.gson:gson:2.8.8")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.5.31")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
    implementation("org.mongodb:mongodb-driver-sync:4.3.2")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}


tasks {
    compileJava {
        options.compilerArgs.add("-parameters")
        options.forkOptions.executable = "javac"
        options.encoding = "UTF-8"
    }

    processResources {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE

        val tokens = mapOf("version" to project.version)
        from(sourceSets["main"].resources.srcDirs) {

            filter<org.apache.tools.ant.filters.ReplaceTokens>("tokens" to tokens)
        }
    }

    shadowJar {
        archiveFileName.set("${project.rootProject.name}-${project.name}-v${project.version}.jar")
        destinationDir = file("$rootDir/output")
    }
}
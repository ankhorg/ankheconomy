plugins {
    id("java")
    id("checkstyle")
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "checkstyle")

    group = "org.inksnow.ankh"
    version = "1.0-SNAPSHOT"

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    repositories {
        mavenCentral()
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") {
            content {
                includeModule("org.bukkit", "bukkit")
                includeModule("org.spigotmc", "spigot-api")
            }
        }
        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/") {
            content {
                includeModule("me.clip", "placeholderapi")
            }
        }
        maven("https://jitpack.io") {
            content {
                includeModule("com.github.MilkBowl", "VaultAPI")
            }
        }
        maven("https://packages.inksnow.org/maven/p/ankh-plugin/maven") {
            content {
                includeModule("bot.inker.inkos", "core")
                includeModule("bot.inker.inkos", "bukkit-api")
            }
        }
        maven("https://jitpack.io/") {
            content {
                includeModule("com.github.MockBukkit", "MockBukkit")
            }
        }
    }

    dependencies {
        checkstyle("com.puppycrawl.tools:checkstyle:10.14.2")
        compileOnly("org.projectlombok:lombok:1.18.32")
        annotationProcessor("org.projectlombok:lombok:1.18.32")

        testCompileOnly("org.projectlombok:lombok:1.18.32")
        testAnnotationProcessor("org.projectlombok:lombok:1.18.32")

        testImplementation(platform("org.junit:junit-bom:5.9.1"))
        testImplementation("org.junit.jupiter:junit-jupiter")
    }

    tasks.test {
        useJUnitPlatform()
    }
}

dependencies {
    implementation("bot.inker.inkos:core:1.0.16")
    implementation("bot.inker.acj:runtime:1.5")
    // implementation("org.yaml:snakeyaml:2.2")

    compileOnly("bot.inker.inkos:bukkit-api:1.0.16")
    compileOnly("org.bukkit:bukkit:1.12.2-R0.1-SNAPSHOT")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("net.luckperms:api:5.4")
    compileOnly("me.clip:placeholderapi:2.11.5")

    testImplementation("com.github.MockBukkit:MockBukkit:v1.16-SNAPSHOT")
}

tasks.processResources {
    filesMatching("plugin.yml") {
        expand("version" to project.version)
    }
}

tasks.test {
    val directory = rootProject.layout.projectDirectory.dir("run").dir("test")

    doFirst {
        mkdir(directory)
    }

    workingDir(directory)
}
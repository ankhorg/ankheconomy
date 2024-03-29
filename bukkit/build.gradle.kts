plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

dependencies {
    implementation(project(":"))

    compileOnly("bot.inker.inkos:bukkit-api:1.0.13")
    compileOnly("org.bukkit:bukkit:1.12.2-R0.1-SNAPSHOT")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("net.luckperms:api:5.4")
    compileOnly("me.clip:placeholderapi:2.11.5")
}

tasks.processResources {
    filesMatching("plugin.yml") {
        expand("version" to project.version)
    }
}

tasks.shadowJar {
    relocate("org.yaml.snakeyaml", "ankh_economy_libs.org.yaml.snakeyaml")

    mergeServiceFiles()
}

tasks.assemble {
    dependsOn(tasks.shadowJar)
}
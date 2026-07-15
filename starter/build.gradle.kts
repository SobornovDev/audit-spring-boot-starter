plugins {
    `java-library`
    `maven-publish`
}
val springBootVersion: String by project

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

dependencies {
    api(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))

    api(project(":autoconfigure"))
    api("org.springframework.boot:spring-boot-starter")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "audit-starter"
            from(components["java"])
        }
    }
}

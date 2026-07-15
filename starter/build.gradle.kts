plugins {
    `java-library`
}
val springBootVersion: String by project

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

dependencies {
    api(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))

    api(project(":audit-spring-boot-autoconfigure"))
    api("org.springframework.boot:spring-boot-starter")
}

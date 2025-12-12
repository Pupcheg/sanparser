plugins {
    java
    id("org.springframework.boot") version "4.0.0"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "me.supcheg"
version = "3.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-liquibase")

    implementation("org.jsoup:jsoup:1.21.2")
    implementation("me.tongfei:progressbar:0.10.1")
    implementation("com.pivovarit:parallel-collectors:3.4.0")
    implementation("com.google.guava:guava:33.5.0-jre")
    implementation("org.apache.poi:poi-ooxml:5.5.1")
    implementation("com.pivovarit:throwing-function:1.6.1")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    runtimeOnly("com.h2database:h2")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

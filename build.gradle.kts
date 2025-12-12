plugins {
    java
    id("org.springframework.boot") version "3.5.4"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "me.supcheg"
version = "2.3.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.liquibase:liquibase-core")

    implementation("org.jsoup:jsoup:1.17.2")
    implementation("me.tongfei:progressbar:0.10.1")
    implementation("com.pivovarit:parallel-collectors:3.2.0")
    implementation("com.google.guava:guava:33.5.0-jre")
    implementation("org.apache.commons:commons-csv:1.12.0")
    implementation("org.apache.poi:poi-ooxml:5.4.1")
    implementation("com.pivovarit:throwing-function:1.6.1")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    runtimeOnly("com.h2database:h2")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

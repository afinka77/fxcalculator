plugins {
    java
    id("org.springframework.boot") version "2.5.4"
}

group = "com.myproject"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.20")
    annotationProcessor("org.projectlombok:lombok:1.18.20")
    implementation("org.springframework.boot:spring-boot-starter:2.5.4")
    implementation("org.springframework.boot:spring-boot-starter-web:2.5.4")
    implementation("org.springframework.boot:spring-boot-starter-validation:2.5.4")
    implementation("com.opencsv:opencsv:5.5.2")
    implementation("org.springdoc:springdoc-openapi-ui:1.5.10")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.12.5")
    runtimeOnly("org.apache.commons:commons-collections4:4.4")

    testCompileOnly("org.projectlombok:lombok:1.18.20")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.20")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.5.4")
    testImplementation("org.testinfected.hamcrest-matchers:hamcrest-matchers:1.8")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
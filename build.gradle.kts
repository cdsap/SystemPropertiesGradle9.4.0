plugins {
    `java-gradle-plugin`
    `maven-publish`
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.4")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.11.4")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
group = "com.example"
version = "0.1.0"

gradlePlugin {
    plugins {
        create("systemProperties") {
            id = "com.example.system-properties"
            implementationClass = "com.example.SystemPropertiesPlugin"
        }
    }
}

publishing {
    repositories {
        mavenLocal()
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

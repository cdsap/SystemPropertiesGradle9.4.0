package com.example;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SystemPropertiesPluginTest {

    @TempDir
    File projectDir;

    @Order(1)
    @ParameterizedTest
    @ValueSource(strings = {"9.3.0", "9.3.1", "9.4.0"})
    void initFile(String gradleVersion) throws IOException {
        Files.writeString(new File(projectDir, "settings.gradle.kts").toPath(), "");
        Files.writeString(new File(projectDir, "build.gradle.kts").toPath(),
            """
                plugins {
                    id("com.example.system-properties")
                }
                repositories {
                    mavenCentral()
                }
                """);

        GradleRunner.create()
            .withProjectDir(projectDir)
            .withPluginClasspath()
            .withArguments("help", "--configuration-cache")
            .withGradleVersion(gradleVersion)
            .build();

    }

    @Order(2)
    @ParameterizedTest
    @ValueSource(strings = {"9.3.0", "9.3.1", "9.4.0"})
    void showSystemPropertiesTaskPrintsProperties(String gradleVersion) throws IOException {
        Files.writeString(new File(projectDir, "settings.gradle.kts").toPath(), "");
        Files.writeString(new File(projectDir, "build.gradle.kts").toPath(),
            """
                plugins {
                    id("com.example.system-properties")
                }
                repositories {
                    mavenCentral()
                }
                """);

        BuildResult firstBuild = GradleRunner.create()
            .withProjectDir(projectDir)
            .withPluginClasspath()
            .withArguments("foo", "--configuration-cache")
            .withGradleVersion(gradleVersion)
            .build();
        assertTrue(firstBuild.getOutput().contains("Configuration cache entry stored."));
        System.out.println(gradleVersion + "-First build output:\n" + firstBuild.getOutput());
        BuildResult secondBuild = GradleRunner.create()
            .withProjectDir(projectDir)
            .withPluginClasspath()
            .withArguments("foo", "--configuration-cache")
            .withGradleVersion(gradleVersion)
            .build();
        System.out.println(gradleVersion + "-Second build output:\n" + secondBuild.getOutput());
        assertTrue(secondBuild.getOutput().contains("Configuration cache entry reused."));
    }
}

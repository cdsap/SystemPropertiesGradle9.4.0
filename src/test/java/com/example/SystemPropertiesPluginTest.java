package com.example;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SystemPropertiesPluginTest {

    @TempDir
    File projectDir;

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

       GradleRunner.create()
            .withProjectDir(projectDir)
            .withPluginClasspath()
            .withArguments("help", "--configuration-cache")
            .withGradleVersion(gradleVersion)
            .build();

        BuildResult result = GradleRunner.create()
            .withProjectDir(projectDir)
            .withPluginClasspath()
            .withArguments("foo", "--configuration-cache")
            .withGradleVersion(gradleVersion)
            .build();
        assertTrue(result.getOutput().contains("Configuration cache entry stored."));

        BuildResult result2 = GradleRunner.create()
            .withProjectDir(projectDir)
            .withPluginClasspath()
            .withArguments("foo", "--configuration-cache")
            .withGradleVersion(gradleVersion)
            .build();
        assertTrue(result2.getOutput().contains("Configuration cache entry reused."));
    }
}

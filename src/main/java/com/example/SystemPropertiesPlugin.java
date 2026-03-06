package com.example;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.provider.Provider;

public class SystemPropertiesPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {

        project.getTasks().register("foo", task -> {
            System.getProperties().forEach((key, value) -> {
            });
        });
    }
}

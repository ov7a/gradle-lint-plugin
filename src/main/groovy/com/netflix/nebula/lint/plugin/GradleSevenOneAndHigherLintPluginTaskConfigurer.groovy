package com.netflix.nebula.lint.plugin

import org.gradle.api.Action
import org.gradle.api.Project

class GradleSevenOneAndHigherLintPluginTaskConfigurer extends GradleSevenZeroLintPluginTaskConfigurer {
    @Override
    Action<GradleLintReportTask> configureReportAction(Project project, GradleLintExtension extension) {
        new Action<GradleLintReportTask>() {
            @Override
            void execute(GradleLintReportTask gradleLintReportTask) {
                gradleLintReportTask.reportOnlyFixableViolations = getReportOnlyFixableViolations(project, extension)
                gradleLintReportTask.notCompatibleWithConfigurationCache("Gradle Lint Plugin is not compatible with configuration cache because it requires project model")

                gradleLintReportTask.reports.all { report ->
                    def fileSuffix = report.name == 'text' ? 'txt' : report.name
                    report.conventionMapping.with {
                        required.set(report.name == getReportFormat(project, extension))
                        outputLocation.set(project.layout.buildDirectory.file("reports/gradleLint/${project.name}.$fileSuffix"))
                    }
                }
            }
        }
    }
}

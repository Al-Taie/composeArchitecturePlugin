package com.altaie.compose.plugin.di

import com.altaie.compose.plugin.core.TemplateGenerator
import com.intellij.openapi.application.Application
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project


class ProjectDependencies(project: Project?) {
    val generator = TemplateGenerator(project!!)
    val editor: FileEditorManager = FileEditorManager.getInstance(project!!)
    val application: Application = ApplicationManager.getApplication()
}
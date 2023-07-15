package com.altaie.compose.plugin.core

import com.altaie.compose.plugin.utils.PropertyKeys
import com.altaie.compose.plugin.utils.getPackageName
import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.fileTemplates.FileTemplateUtil
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import java.util.*


class TemplateGenerator(private val project: Project) {

    fun generateKt(
            templateName: String,
            fileName: String,
            directory: PsiDirectory,
            properties: MutableMap<String,Any>
    ) : PsiFile {

        val existing = directory.findFile("${fileName}.kt")
        if (existing != null) return existing


        val manager = FileTemplateManager.getInstance(project)
        val template = manager.getInternalTemplate(templateName)
        properties[PropertyKeys.PACKAGE_NAME] = requireNotNull(directory.getPackageName())
        return FileTemplateUtil.createFromTemplate(
                template,
                "${fileName}.kt",
                properties.toProperties(),
                directory
        ) as PsiFile
    }

    private fun Map<String, Any>.toProperties(): Properties {
        return Properties().apply {
            this@toProperties.forEach { setProperty(it.key, it.value.toString()) }
        }
    }
}


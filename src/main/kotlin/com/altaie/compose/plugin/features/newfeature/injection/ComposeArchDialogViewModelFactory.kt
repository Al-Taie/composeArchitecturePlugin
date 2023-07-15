package com.altaie.compose.plugin.features.newfeature.injection

import com.intellij.psi.PsiDirectory
import com.altaie.compose.plugin.di.ProjectDependencies
import com.altaie.compose.plugin.features.newfeature.ComposeDialogViewModel

object ComposeArchDialogViewModelFactory {

    fun create(psiDirectory: PsiDirectory, dependencies: ProjectDependencies) : ComposeDialogViewModel {
        return ComposeDialogViewModel(
            directory = psiDirectory,
            generator = dependencies.generator,
            editorManager = dependencies.editor,
            application = dependencies.application
        )
    }
}
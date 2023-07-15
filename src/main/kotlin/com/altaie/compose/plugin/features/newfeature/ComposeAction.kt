package com.altaie.compose.plugin.features.newfeature

import com.altaie.compose.plugin.di.ProjectDependencies
import com.altaie.compose.plugin.features.newfeature.injection.ComposeArchDialogViewModelFactory
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.psi.PsiDirectory

class ComposeAction: AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val deps = ProjectDependencies(e.project)
        val directory = e.getData(CommonDataKeys.PSI_ELEMENT) as PsiDirectory
        val viewModel = ComposeArchDialogViewModelFactory.create(directory, deps)
        ComposeDialog(viewModel).show()
    }
}
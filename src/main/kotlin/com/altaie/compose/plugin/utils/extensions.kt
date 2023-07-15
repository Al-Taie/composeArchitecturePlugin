package com.altaie.compose.plugin.utils

import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.psi.PsiDirectory


fun PsiDirectory.getPackageName(): String? {
    return ProjectRootManager.getInstance(project)
        .fileIndex
        .getPackageNameByDirectory(virtualFile)
}

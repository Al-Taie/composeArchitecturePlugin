package com.altaie.compose.plugin.utils

import com.intellij.psi.PsiDirectory


fun PsiDirectory.getPackageName(): String? {
    val path = virtualFile.canonicalPath?.replace("/", ".").orEmpty()
    val pattern = ".*?(?:kotlin|java)\\.(.*?)$".toRegex()
    val result = pattern.find(path)?.groupValues?.get(1)
    return result
}

val Boolean?.isTrue: Boolean
    get() = this == true

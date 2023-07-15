package com.altaie.compose.plugin.features.newfeature

import com.altaie.compose.plugin.core.BaseViewModel
import com.altaie.compose.plugin.utils.PropertyKeys
import com.altaie.compose.plugin.core.TemplateGenerator
import com.altaie.compose.plugin.utils.getPackageName
import com.intellij.openapi.application.Application
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.psi.PsiDirectory
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch


class ComposeDialogViewModel(
    private val directory: PsiDirectory,
    private val generator: TemplateGenerator,
    private val editorManager: FileEditorManager,
    private val application: Application
) : BaseViewModel() {

    var name: String = ""
        get() = field.replaceFirstChar(Char::titlecase)

    val successFlow = MutableSharedFlow<Unit>()

    var createFeaturePackages: Boolean = true

    fun onOkButtonClick() {
        application.runWriteAction {
            val featPackage = directory.createSubdirectory(name.lowercase())

            if (createFeaturePackages)
                createPackages(featPackage)

            if (directory.findSubdirectory("base") == null)
                createBaseClasses()

            createUtils()

            val properties = mutableMapOf<String, Any>(
                PropertyKeys.NAME to name,
                PropertyKeys.FEATURE_NAME to name.replaceFirstChar(Char::lowercase),
                PropertyKeys.BASE_PACKAGE_NAME to directory.getPackageName().orEmpty(),
            )

            val file = generator.generateKt(
                templateName = "ComposeUiState",
                fileName = "${name}UiState",
                directory = featPackage.findSubdirectory("state") ?: featPackage,
                properties = properties
            )

            generator.generateKt(
                templateName = "ComposeEvent",
                fileName = "${name}Event",
                directory = featPackage.findSubdirectory("event") ?: featPackage,
                properties = properties
            )

            generator.generateKt(
                templateName = "ComposeInteractionListener",
                fileName = "${name}InteractionListener",
                directory = featPackage.findSubdirectory("listener") ?: featPackage,
                properties = properties
            )

            generator.generateKt(
                templateName = "ComposeScreen",
                fileName = "${name}Screen",
                directory = featPackage.findSubdirectory("ui") ?: featPackage,
                properties = properties
            )

            generator.generateKt(
                templateName = "ComposeViewModel",
                fileName = "${name}ViewModel",
                directory = featPackage.findSubdirectory("viewModel") ?: featPackage,
                properties = properties
            )

            editorManager.openFile(file.virtualFile, true)
            scope.launch { successFlow.emit(Unit) }
        }
    }

    private fun createPackages(featPackage: PsiDirectory) {
        val packages = listOf("event", "ui", "state", "viewModel", "listener")
        packages.forEach(featPackage::createSubdirectory)

        featPackage.findSubdirectory("ui")
            ?.createSubdirectory("components")
            ?.createFile(".keep")
    }

    private fun createBaseClasses() {
        directory.parentDirectory?.createSubdirectory("base")?.let { base ->
            generator.generateKt(
                templateName = "ComposeErrorState",
                fileName = "ErrorState",
                directory = base,
                properties = mutableMapOf()
            )

            generator.generateKt(
                templateName = "ComposeBaseViewModel",
                fileName = "BaseViewModel",
                directory = base,
                properties = mutableMapOf()
            )
        }
    }

    private fun createUtils() {
        val utils = with(directory) {
            findSubdirectory("utils") ?: createSubdirectory("utils")
        }

        val extensions = with(utils) {
            findSubdirectory("extensions") ?: createSubdirectory("extensions")
        }

        if (extensions.findFile("eventListener.kt") != null)
            return

        generator.generateKt(
            templateName = "ComposeEventListener",
            fileName = "eventListener",
            directory = extensions,
            properties = mutableMapOf()
        )
    }
}

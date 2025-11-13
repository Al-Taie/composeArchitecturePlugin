package com.altaie.compose.plugin.features.newfeature

import com.altaie.compose.plugin.core.BaseViewModel
import com.altaie.compose.plugin.core.TemplateGenerator
import com.altaie.compose.plugin.utils.PropertyKeys
import com.altaie.compose.plugin.utils.getPackageName
import com.altaie.compose.plugin.utils.isTrue
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

    val stateFlow = MutableSharedFlow<ComposeDialogState>()
    var createFeaturePackages: Boolean = false

    val properties: MutableMap<String, Any>
        get() = mutableMapOf(
            PropertyKeys.NAME to name,
            PropertyKeys.FEATURE_NAME to name.replaceFirstChar(Char::lowercase),
            PropertyKeys.BASE_PACKAGE_NAME to directory.getPackageName().orEmpty().replace(".features", ""),
            PropertyKeys.FEATURES_PACKAGE_NAME to directory.getPackageName().orEmpty(),
        )

    fun onOkButtonClick() {
        application.runActionCatching(
            onFailure = { emitState(ComposeDialogState.Error(message = it.message.orEmpty())) }
        ) {
            val packageName = directory.getPackageName()
            val isWithinFeaturesDir = packageName?.endsWith("presentation.features").isTrue
            if (isWithinFeaturesDir.not())
                error("Not allowed! You should call it at [presentation/features] only.")

            emitState(ComposeDialogState.Loading)

            val featPackage = directory.createSubdirectory(name.lowercase())

            if (createFeaturePackages)
                createPackages(featPackage)

            if (directory.findSubdirectory("base") == null)
                createBaseClasses()

            createUtils()

            val file = generator.generateKt(
                templateName = "ComposeUiState",
                fileName = "${name}UiState",
                directory = featPackage,
                properties = properties
            )

            generator.generateKt(
                templateName = "ComposeEffect",
                fileName = "${name}Effect",
                directory = featPackage,
                properties = properties
            )

            generator.generateKt(
                templateName = "ComposeActions",
                fileName = "${name}Actions",
                directory = featPackage,
                properties = properties
            )

            generator.generateKt(
                templateName = "ComposeScreen",
                fileName = "${name}Screen",
                directory = featPackage,
                properties = properties
            )

            generator.generateKt(
                templateName = "ComposeViewModel",
                fileName = "${name}ViewModel",
                directory = featPackage,
                properties = properties
            )

            editorManager.openFile(file.virtualFile, true)
            emitState(ComposeDialogState.Success)
        }
    }

    private fun emitState(state: ComposeDialogState) = scope.launch { stateFlow.emit(state) }

    private fun createPackages(featPackage: PsiDirectory) {
        val packages = listOf("effect", "ui", "state", "viewModel", "listener")
        packages.forEach(featPackage::createSubdirectory)

        featPackage.findSubdirectory("ui")
            ?.createSubdirectory("components")
            ?.createFile(".keep")
    }

    private fun createBaseClasses() {
        directory.createSubdirectory("base").let { base ->
            generator.generateKt(
                templateName = "ComposeErrorState",
                fileName = "ErrorState",
                directory = base,
                properties = properties
            )

            generator.generateKt(
                templateName = "ComposeBaseViewModel",
                fileName = "BaseViewModel",
                directory = base,
                properties = properties
            )
        }
    }

    private fun createUtils() {
        val utils = directory.parentDirectory?.run {
            findSubdirectory("utils") ?: createSubdirectory("utils")
        } ?: error("Cannot create utils package")

        val extensions = with(utils) {
            findSubdirectory("extensions") ?: createSubdirectory("extensions")
        }

        if (extensions.findFile("effectListener.kt") != null)
            return

        generator.generateKt(
            templateName = "ComposeEffectListener",
            fileName = "effectListener",
            directory = extensions,
            properties = properties
        )
    }
}

private fun Application.runActionCatching(
    onFailure: (Throwable) -> Unit,
    runnable: Runnable
) = runCatching { runWriteAction(runnable) }.onFailure(onFailure)

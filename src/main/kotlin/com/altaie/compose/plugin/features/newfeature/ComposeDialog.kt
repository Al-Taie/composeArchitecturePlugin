package com.altaie.compose.plugin.features.newfeature

import com.altaie.compose.plugin.core.BaseDialog
import com.intellij.openapi.progress.util.ProgressDialog
import com.intellij.openapi.progress.util.ProgressWindow
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.Messages
import com.intellij.ui.dsl.builder.bindItem
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.builder.whenTextChangedFromUi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@Suppress("UnstableApiUsage")
class ComposeDialog(
    private val viewModel: ComposeDialogViewModel,
) : BaseDialog() {
    private val project by lazy { ProjectManager.getInstance().defaultProject }
    val progressDialog = ProgressDialog(
        ProgressWindow(false, project).apply {
            this.title = "Creating new feature"
            this.text = "Please wait..."
            this.text2 = this.text
        },
        myShouldShowBackground = false,
        cancelText = null,
        myParentWindow = null
    )

    private var selectedScreenContent: ScreenContent?
        get() = viewModel.screenContent
        set(value) {
            viewModel.screenContent = value ?: ScreenContent.EMPTY
        }

    init {
        title = "New Jetpack Compose Feature"
        init()
        viewModel
            .stateFlow
            .onEach { state ->
                when (state) {
                    ComposeDialogState.Loading -> progressDialog.show()
                    ComposeDialogState.Success -> {
                        progressDialog.hide()
                        close(0)
                    }

                    is ComposeDialogState.Error -> {
                        progressDialog.hide()
                        Messages.showMessageDialog(state.message, "Error", Messages.getErrorIcon())
                        repaint()
                    }
                }
            }
            .launchIn(dialogScope)
    }

    override fun createPanel(): DialogPanel {
        return panel {
            row("Feature Name:") {
                textField()
                    .whenTextChangedFromUi {
                        viewModel.name = it
                    }
                    .focused()
            }
            row("Screen Content") {
                comboBox(items = ScreenContent.entries) { _, value, _, _, _ ->
                    text(value?.displayName.toString()).component
                }.bindItem(::selectedScreenContent)
            }

            row {
                checkBox(
                    text = "Create packages",
                ).bindSelected(viewModel::createFeaturePackages)
            }
        }
    }

    override fun doOKAction() {
        panel.apply()
        viewModel.onOkButtonClick()
    }
}

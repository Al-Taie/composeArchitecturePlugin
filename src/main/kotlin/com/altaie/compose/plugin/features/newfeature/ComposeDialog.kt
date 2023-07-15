package com.altaie.compose.plugin.features.newfeature

import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.panel
import com.altaie.compose.plugin.core.BaseDialog
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.whenTextChangedFromUi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Suppress("UnstableApiUsage")
class ComposeDialog(
    private val viewModel: ComposeDialogViewModel,
) : BaseDialog() {

    init {
        init()
        viewModel
            .successFlow
            .onEach { close(0) }
            .launchIn(dialogScope)
    }

    override fun createPanel(): DialogPanel {
        return panel {
            row {
                label(text = "New Jetpack Compose Feature")
                textField()
                    .whenTextChangedFromUi {
                        viewModel.name = it
                    }
                    .focused()
            }

            row {
                label(text = "Options")
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

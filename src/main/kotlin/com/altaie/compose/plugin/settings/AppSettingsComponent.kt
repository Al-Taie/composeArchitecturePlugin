package com.altaie.compose.plugin.settings

import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel

class AppSettingsComponent {

    val panel: JPanel
    private val rootPackageField = JBTextField()

    var rootPackage: String
        get() = rootPackageField.text
        set(value) {
            rootPackageField.text = value
        }

    val preferredFocusedComponent: JComponent
        get() = rootPackageField

    init {
        panel = FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel("Enter root package: "), rootPackageField, 1, false)
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }
}

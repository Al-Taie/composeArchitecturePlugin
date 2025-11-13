package com.altaie.compose.plugin.settings

import com.intellij.openapi.options.Configurable
import javax.swing.JComponent

class AppSettingsConfigurable : Configurable {

    private var mySettingsComponent: AppSettingsComponent? = null

    override fun getDisplayName(): String = "Compose Architecture"

    override fun getPreferredFocusedComponent(): JComponent? = mySettingsComponent?.preferredFocusedComponent

    override fun createComponent(): JComponent? {
        mySettingsComponent = AppSettingsComponent()
        return mySettingsComponent?.panel
    }

    override fun isModified(): Boolean {
        val settings = AppSettingsState.instance
        //return mySettingsComponent?.rootPackage != settings.rootPackage
        return false
    }

    override fun apply() {
        val settings = AppSettingsState.instance
        //settings.rootPackage = mySettingsComponent?.rootPackage ?: ""
    }

    override fun reset() {
        val settings = AppSettingsState.instance
        //mySettingsComponent?.rootPackage = settings.rootPackage
    }

    override fun disposeUIResources() {
        mySettingsComponent = null
    }
}

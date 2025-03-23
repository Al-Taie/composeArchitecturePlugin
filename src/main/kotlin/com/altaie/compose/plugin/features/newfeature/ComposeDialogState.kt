package com.altaie.compose.plugin.features.newfeature


sealed interface ComposeDialogState {
    object Loading : ComposeDialogState
    object Success : ComposeDialogState
    data class Error(val message: String) : ComposeDialogState
}

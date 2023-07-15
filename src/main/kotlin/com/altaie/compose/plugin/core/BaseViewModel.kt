package com.altaie.compose.plugin.core

import com.altaie.compose.plugin.di.Dependencies


abstract class BaseViewModel {
    protected val scope = Dependencies.ioScope
}
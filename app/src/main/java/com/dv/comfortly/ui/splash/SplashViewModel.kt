package com.dv.comfortly.ui.splash

import androidx.lifecycle.SavedStateHandle
import com.dv.comfortly.ui.base.BaseViewModel
import com.dv.comfortly.ui.base.EmptyState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel<EmptyState, Nothing>(EmptyState)

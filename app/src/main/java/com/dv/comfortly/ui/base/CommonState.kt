package com.dv.comfortly.ui.base

import androidx.annotation.StringRes
import com.dv.comfortly.R

sealed class CommonState

object Idle : CommonState()

object Loading : CommonState()

data class Error(
    @StringRes val message: Int = R.string.error_message_generic,
    val throwable: Throwable? = null,
) : CommonState()

data class ViewState<out State : Any>(
    val state: State,
    var commonState: CommonState = Idle,
)

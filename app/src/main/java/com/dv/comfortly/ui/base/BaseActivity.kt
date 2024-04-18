package com.dv.comfortly.ui.base

import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

abstract class BaseActivity<State : Any, Event : Any> : AppCompatActivity() {
    abstract val viewModel: BaseViewModel<State, Event>?

    open val viewBinding: ViewBinding? = null

    private val loadingDialog: DialogFragment by lazy {
        LoadingDialog().apply {
            isCancelable = false
        }
    }

    private var lastState: State? = null
    private var lastCommonState: CommonState = Idle

    open fun renderState(state: State) = Unit

    open fun handleEvent(event: Event) = Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        viewBinding?.let {
            setContentView(it.root)
        }

        lifecycleScope.launch {
            viewModel?.viewStates()
                ?.onEach {
                    if (it.commonState != lastCommonState) {
                        lastCommonState = it.commonState
                        handleCommonState(it.commonState)
                    }

                    if (it.state != lastState) {
                        lastState = it.state
                        renderState(it.state)
                    }
                }?.collect()
        }
        lifecycleScope.launch {
            viewModel?.viewEvents()
                ?.onEach { handleEvent(it) }
                ?.collect()
        }
    }

    private fun handleCommonState(commonState: CommonState) {
        when (commonState) {
            is Idle -> hideLoading()
            is Error -> {
                hideLoading()
                AlertDialog.Builder(this)
                    .setTitle(commonState.message)
                    .setMessage(commonState.throwable?.message)
                    .show()
            }
            is Loading -> showLoading()
        }
    }

    protected fun showLoading() {
        loadingDialog.show(supportFragmentManager, null)
    }

    protected fun hideLoading() {
        loadingDialog.dismiss()
    }

    fun setToolbar(
        toolbar: Toolbar?,
        @StringRes title: Int? = null,
        titleText: String? = null,
    ) {
        toolbar?.let {
            setSupportActionBar(toolbar)
            title?.let {
                setTitle(it)
            } ?: titleText?.let {
                setTitle(it)
            }
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
}

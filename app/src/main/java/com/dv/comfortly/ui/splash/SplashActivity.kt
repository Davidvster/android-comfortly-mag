package com.dv.comfortly.ui.splash

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.dv.comfortly.R
import com.dv.comfortly.ui.base.BaseActivity
import com.dv.comfortly.ui.base.EmptyState
import com.dv.comfortly.ui.dashboard.DashboardActivity
import com.dv.comfortly.ui.ext.showDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity<EmptyState, Nothing>() {

    override val viewModel: SplashViewModel by viewModels()

    private val appPermissions by lazy {
        listOfNotNull(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) Manifest.permission.BLUETOOTH_SCAN else null,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) Manifest.permission.BLUETOOTH_CONNECT else null
        )
    }

    private val requestPermissionRequest = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
        if (!result.filter { it.value }.keys.containsAll(appPermissions)) {
            showDialog(
                title = R.string.require_permissions_title,
                message = R.string.require_permissions_message,
                positiveButtonText = R.string.retry,
                positiveButtonListener = { requestPermissions() },
                negativeButtonText = R.string.close_app,
                negativeButtonListener = { finishAffinity() }
            )
        } else {
            toDashboard()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (appPermissions.map { checkSelfPermission(it) }.any { it != PackageManager.PERMISSION_GRANTED }) {
            requestPermissions()
        } else {
            toDashboard()
        }
    }

    private fun requestPermissions() {
        requestPermissionRequest.launch(appPermissions.toTypedArray())
    }

    private fun toDashboard() {
        startActivity(DashboardActivity.newIntent(this))
        finish()
    }
}

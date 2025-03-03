package com.example.vibe.utils

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat


@Composable
fun CameraPermissionRequest(
    onPermissionGranted: @Composable () -> Unit
) {
    val context = LocalContext.current
    val cameraPermission = Manifest.permission.CAMERA

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                // ✅ Proceed to the scanner if permission is granted
            } else {
                Toast.makeText(context, "Camera permission is required to scan QR codes.", Toast.LENGTH_LONG).show()
            }
        }
    )

    val permissionState = ContextCompat.checkSelfPermission(context, cameraPermission)

    when {
        permissionState == PackageManager.PERMISSION_GRANTED -> {
            onPermissionGranted() // ✅ If permission is already granted, proceed
        }
        else -> {
            LaunchedEffect(Unit) { // ✅ Automatically launches permission request
                permissionLauncher.launch(cameraPermission)
            }
        }
    }
}

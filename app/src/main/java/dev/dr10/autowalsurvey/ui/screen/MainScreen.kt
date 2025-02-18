package dev.dr10.autowalsurvey.ui.screen

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import dev.dr10.autowalsurvey.domain.model.ImageInfoModel
import dev.dr10.autowalsurvey.ui.components.CodeActionsComponent
import dev.dr10.autowalsurvey.ui.components.PendingImagesComponent
import dev.dr10.autowalsurvey.ui.components.WebViewComponent
import dev.dr10.autowalsurvey.ui.theme.AppTheme
import dev.dr10.autowalsurvey.ui.viewModel.MainViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) = Scaffold(Modifier.fillMaxSize()) { padding ->

    val state = viewModel.state.collectAsState().value
    val context = LocalContext.current
    var code by remember { mutableStateOf("") }
    var startProcess by remember { mutableStateOf(false) }
    var currentImageIndex by remember { mutableIntStateOf(0) }
    var imagesToProcess by remember { mutableStateOf(emptyList<ImageInfoModel>()) }

    LaunchedEffect(currentImageIndex) {
        if (currentImageIndex < imagesToProcess.size) {
            code = imagesToProcess[currentImageIndex].code
            startProcess = true
        }
    }

    val imageCropLauncher = rememberLauncherForActivityResult(contract = CropImageContract()) { result ->
        if (result.isSuccessful) {
            result.uriContent?.let { viewModel.addImage(it, context) }
        }
    }

    val cameraPermissionState = rememberPermissionState(
        permission = Manifest.permission.CAMERA,
        onPermissionResult = { isGranted ->
            if (isGranted) {
                val cropOptions = CropImageContractOptions(
                    null,
                    CropImageOptions(
                        imageSourceIncludeGallery = false
                    )
                )
                imageCropLauncher.launch(cropOptions)
            }
            else Log.e("MainScreen", "CAMERA::PERMISSION::DENIED")
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .background(AppTheme.colors.background)
    ) {
        Spacer(Modifier.height(12.dp))

        if (startProcess) {
            WebViewComponent(
                modifier = Modifier.weight(1f),
                viewModel = viewModel,
                code = code,
                startProcess = startProcess
            ) {
                startProcess = false
                viewModel.deleteImage(imagesToProcess[currentImageIndex])
                if (currentImageIndex < imagesToProcess.size - 1) currentImageIndex++
            }

            Spacer(modifier = Modifier.height(8.dp))
        } else Spacer(modifier = Modifier.weight(1f))

        PendingImagesComponent(
            state.imagesPending,
            context,
            onUpdateCode = { uri, code -> viewModel.updateCode(code, uri) },
            onDelete = { viewModel.deleteImage(it) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        CodeActionsComponent(
            counter = state.counter,
            isProcessing = startProcess,
            onCameraLauncher = { cameraPermissionState.launchPermissionRequest() },
            onGalleryLauncher = { 
                val cropOptions = CropImageContractOptions(
                    null,
                    CropImageOptions(
                        imageSourceIncludeCamera = false,
                        toolbarColor = AppTheme.colors.background.toArgb(),
                        activityBackgroundColor = AppTheme.colors.background.toArgb(),
                        toolbarTitleColor = AppTheme.colors.text.toArgb(),
                        progressBarColor = AppTheme.colors.complementary.toArgb()
                    )
                )
                imageCropLauncher.launch(cropOptions)
            },
            onRestartState = {
                startProcess = false
                viewModel.clearPendingImages()
            },
            onStartProcess = {
                imagesToProcess = viewModel.getAllImagesToProcess()
                if (imagesToProcess.isEmpty()) return@CodeActionsComponent
                currentImageIndex = 0
                code = imagesToProcess[currentImageIndex].code
                startProcess = true
            },
            onResetCounter = { viewModel.clearCounter() }
        )

        Spacer(Modifier.height(12.dp))

    }

}
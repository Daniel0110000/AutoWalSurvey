package dev.dr10.autowalsurvey.ui.screen

import android.Manifest
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import dev.dr10.autowalsurvey.ui.components.CodeActionsComponent
import dev.dr10.autowalsurvey.ui.components.ImagePreviewComponent
import dev.dr10.autowalsurvey.ui.components.WebViewComponent
import dev.dr10.autowalsurvey.ui.viewModel.MainViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) = Scaffold(Modifier.fillMaxSize()) { padding ->

    val state = viewModel.state.collectAsState().value
    val context = LocalContext.current
    var bitmap: Bitmap? by remember { mutableStateOf(null) }
    var startProcess by remember { mutableStateOf(false) }

    val imageCropLauncher = rememberLauncherForActivityResult(contract = CropImageContract()) { result ->
        if (result.isSuccessful) {
            result.uriContent?.let {
                viewModel.extractTextFromImage(it, context)
                bitmap = if (Build.VERSION.SDK_INT < 28) {
                    MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                } else {
                    val source = ImageDecoder.createSource(context.contentResolver, it)
                    ImageDecoder.decodeBitmap(source)
                }
            }
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
            else Log.d("MainScreen", "Camera permission denied")
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        Spacer(Modifier.height(12.dp))

        if (startProcess) {
            WebViewComponent(
                modifier = Modifier.weight(1f),
                viewModel = viewModel,
                startProcess = startProcess
            ) {
                startProcess = false
                viewModel.setCode("")
                bitmap = null
            }

            Spacer(modifier = Modifier.height(8.dp))
        }


        ImagePreviewComponent(bitmap)

        CodeActionsComponent(
            value = state.code,
            isProcessing = startProcess,
            onValueChange = { viewModel.setCode(it) },
            onCameraLauncher = { cameraPermissionState.launchPermissionRequest() },
            onGalleryLauncher = { 
                val cropOptions = CropImageContractOptions(
                    null,
                    CropImageOptions(imageSourceIncludeCamera = false)
                )
                imageCropLauncher.launch(cropOptions)
            },
            onReloadPage = {
                startProcess = false
                viewModel.setCode("")
                bitmap = null
            },
            onStartProcess = { startProcess = true }
        )

        Spacer(Modifier.height(12.dp))

    }

}
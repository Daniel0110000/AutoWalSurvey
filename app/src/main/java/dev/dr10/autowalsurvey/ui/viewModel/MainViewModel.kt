package dev.dr10.autowalsurvey.ui.viewModel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.dr10.autowalsurvey.domain.model.ImageInfoModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {

    private val _state: MutableStateFlow<MainState> = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    val payloadForExtractProgress = "document.getElementsByClassName(\"progress-text\")[0].textContent;"

    val payloadByProgress: Map<String, String> = mapOf(
        "37" to "document.getElementById(\"btn_continue\").click();",
        "50" to "document.getElementsByClassName(\"atmrating-last\")[0].click();",
        "52" to "document.getElementById(\"ans2353.0.0\").value = \"Buen servicio\"; document.getElementById(\"btn_continue\").click();",
        "53" to "document.getElementsByClassName(\"atmrating-last\")[0].click();",
        "55" to "document.getElementsByClassName(\"atmrating-last\")[0].click();",
        "57" to "document.querySelectorAll(\".atmrating-last\").forEach(element => { element.click(); });",
        "60" to "document.getElementsByClassName(\"atmrating-last\")[0].click();",
        "62" to "document.getElementById(\"ans319.0.1\").click();",
        "64" to "document.getElementById(\"ans1663.0.1\").click(); document.getElementById(\"btn_continue\").click();",
        "84" to "document.getElementById(\"ans1670.0.1\").click();",
        "86" to "document.getElementById(\"ans1412.0.1\").click(); document.getElementById(\"btn_continue\").click();",
        "98" to "document.getElementById(\"btn_continue\").click();",
    )

    val payloadForVerifiedCompleted = "document.getElementsByClassName(\"exit-message-header\").length == 1;"

    data class MainState(
        val imagesPending: List<ImageInfoModel> = mutableListOf()
    )

    fun addImage(uri: Uri, context: Context) {
        val image: InputImage = InputImage.fromFilePath(context, uri)
        recognizer.process(image)
            .addOnSuccessListener {
                val textExtracted = it.text.replace("\\s".toRegex(), "").replace("O", "0")
                updateState {
                    copy(
                        imagesPending = (imagesPending + ImageInfoModel(uri = uri, code = textExtracted)).toMutableList()
                    )
                }
            }
            .addOnFailureListener { e ->
                Log.e("MainViewModel", "ERROR::EXTRACTING::TEXT::FROM::IMAGE::${e.message}")
            }
    }

    fun getAllImagesToProcess(): List<ImageInfoModel> = _state.value.imagesPending

    fun updateCode(newCode: String, uri: Uri) {
        val newPendingImages = _state.value.imagesPending.map { info ->
            if (info.uri == uri) info.copy(code = newCode)
            else info
        }
        updateState { copy(imagesPending = newPendingImages) }
    }

    fun deleteImage(model: ImageInfoModel) {
        val newPendingImages = _state.value.imagesPending.filter { it != model }
        updateState { copy(imagesPending = newPendingImages) }
    }

    fun clearPendingImages() {
        updateState { copy(imagesPending = emptyList()) }
    }

    fun getFirstPayload(code: String): String {
        return if (code.isNotBlank()) "document.getElementById(\"ans1514.0.1\").value = \"$code\"; document.getElementById(\"btn_continue\").click();"
        else ""
    }

    private fun updateState(update: MainState.() -> MainState) {
        _state.value = _state.value.update()
    }
}
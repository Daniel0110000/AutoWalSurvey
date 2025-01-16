package dev.dr10.autowalsurvey.ui.viewModel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.dr10.autowalsurvey.domain.utils.Constants
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
        val code: String = ""
    )

    fun extractTextFromImage(uri: Uri, context: Context) {
        val image: InputImage = InputImage.fromFilePath(context, uri)
        recognizer.process(image)
            .addOnSuccessListener { setCode(it.text.replace("\\s".toRegex(), "").replace("O", "0")) }
            .addOnFailureListener { e ->
                Log.e("MainViewModel", "Error extracting text from image: ${e.message}")
            }
    }

    fun getFirstPayload(): String {
        val code = _state.value.code
        return if (code.isNotBlank()) "document.getElementById(\"ans1514.0.1\").value = \"$code\"; document.getElementById(\"btn_continue\").click();"
        else ""
    }

    fun setCode(code: String) {
        updateState { copy(code = code) }
    }

    private fun updateState(update: MainState.() -> MainState) {
        _state.value = _state.value.update()
    }
}
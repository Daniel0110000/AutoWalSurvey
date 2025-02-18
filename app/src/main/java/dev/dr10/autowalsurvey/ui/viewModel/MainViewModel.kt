package dev.dr10.autowalsurvey.ui.viewModel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.dr10.autowalsurvey.domain.model.ImageInfoModel
import dev.dr10.autowalsurvey.domain.repository.MainRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository
): ViewModel() {

    private val _state: MutableStateFlow<MainState> = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    val payloadForExtractProgress = "document.querySelector(\".progressbar_progressBarIndicator\").style.width;"
    val payloadButtonNext = "document.getElementById(\"buttonNext\").click();"

    val payloadByProgress: Map<String, String> = mapOf(
        "12" to "document.getElementById(\"onf_q_wmcam_brick_ltr_scale11_10\").click(); document.getElementById(\"spl_q_wmcam_brick_comentario_ltr_promotor_cmt\").value = \"Buen Servicio\"; document.getElementById(\"onf_q_wmcam_brick_flex_yn_2\").click();",
        "87" to "document.getElementById(\"onf_q_wmcam_sorteo_trabaja_en_wm_yn_2\").click(); document.getElementById(\"onf_q_wmcam_sorteo_participar_yn_2\").click(); document.getElementById(\"onf_q_wmcam_sorteo_comunicaciones_yn_2\").click(); document.getElementById(\"buttonFinish\").click();"
    )

    init {
        viewModelScope.launch {
            repository.getSurveyCounter().collect { updateState { copy(counter = it) } }
        }
    }

    data class MainState(
        val imagesPending: List<ImageInfoModel> = mutableListOf(),
        val counter: Int = 0
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

    fun addSurvey() {
        viewModelScope.launch { repository.addSurvey() }
    }

    fun clearCounter() {
        viewModelScope.launch { repository.clearCounter() }
    }

    fun deleteImage(model: ImageInfoModel) {
        val newPendingImages = _state.value.imagesPending.filter { it != model }
        updateState { copy(imagesPending = newPendingImages) }
    }

    fun clearPendingImages() {
        updateState { copy(imagesPending = emptyList()) }
    }

    fun getFirstPayload(code: String): String {
        return if (code.isNotBlank()) "document.getElementById(\"spl_q_wmcam_brick_num_ticket_txt\").value = \"$code\"; document.getElementById(\"buttonBegin\").click();"
        else ""
    }

    private fun updateState(update: MainState.() -> MainState) {
        _state.value = _state.value.update()
    }
}
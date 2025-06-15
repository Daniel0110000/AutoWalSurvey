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
        "11" to "${"onf_q_wmcam_brick_ltr_scale11_9".clickElementById()} ${getCommentValue()}",
        "33" to "onf_q_wmcam_brick_flex_yn_1".clickElementById(),
        "44" to "onf_q_wmcam_brick_percepcion_precios_enum_21".clickElementById(),
        "55" to "${"onf_q_wmcam_brick_depto_abarrotes_yn_1".clickElementById()} ${"onf_q_wmcam_brick_ltr_abarrotes_scale11_9v".clickElementById()} ${"onf_q_wmcam_brick_razon_promotor_abarrotes_enum_1".clickElementById()}",
        "66" to "${"onf_q_wmcam_brick_infra_limpieza_scale11_9".clickElementById()} ${"onf_q_wmcam_brick_infra_temperatura_scale11_9".clickElementById()} ${"onf_q_wmcam_brick_infra_rapidez_entrada_salida_scale11_9".clickElementById()} ${"onf_q_wmcam_brick_infra_instalaciones_scale11_9".clickElementById()} ${"onf_q_wmcam_brick_infra_amabilidad_scale11_9".clickElementById()} ${"onf_q_wmcam_brick_infra_iluminacion_scale11_9".clickElementById()} ${"onf_q_wmcam_brick_infra_guardias_seguridad_scale11_9".clickElementById()}",
        "77" to "${"onf_q_wmcam_brick_infra_estacionamiento_scale11na_9".clickElementById()} ${"onf_q_wmcam_brick_infra_personal_recibir_ayuda_scale11na_8".clickElementById()} ${"onf_q_wmcam_brick_infra_disponibilidad_carritos_scale11na_9".clickElementById()} ${"onf_q_wmcam_brick_infra_limpieza_banos_scale11na_11".clickElementById()} ${"onf_q_wmcam_brick_infra_servicio_atencion_scale11na_9".clickElementById()}", // onf_q_wmcam_brick_infra_estacionamiento_scale11na_9, c, onf_q_wmcam_brick_infra_disponibilidad_carritos_scale11na_9, c, onf_q_wmcam_brick_infra_servicio_atencion_scale11na_9
        "88" to "${"onf_q_wmcam_sorteo_trabaja_en_wm_yn_2".clickElementById()} ${"onf_q_wmcam_sorteo_participar_yn_2".clickElementById()} ${"onf_q_wmcam_sorteo_comunicaciones_yn_2".clickElementById()} ${"buttonFinish".clickElementById()}"
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

    fun getCommentValue(): String {
        val comments = listOf(
            "Buen servicio",
            "Atención de calidad",
            "Excelente trato al cliente",
            "Servicio impecable",
            "Atención profesional",
            "Trato cordial y eficiente",
            "Asistencia oportuna y efectiva",
            "Servicio sobresaliente",
            "Atención personalizada y amable",
            "Experiencia satisfactoria",
            "Servicio rápido y confiable"
        )

        return "document.getElementById(\"spl_q_wmcam_brick_comentario_ltr_promotor_cmt\").value = \"${comments.random()}\";"
    }

    fun String.clickElementById(): String = "document.getElementById(\"${this}\").click();"
}
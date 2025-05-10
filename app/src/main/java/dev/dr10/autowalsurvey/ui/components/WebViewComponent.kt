package dev.dr10.autowalsurvey.ui.components

import android.annotation.SuppressLint
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import dev.dr10.autowalsurvey.domain.utils.Constants
import dev.dr10.autowalsurvey.domain.utils.clearNumber
import dev.dr10.autowalsurvey.ui.viewModel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewComponent(
    modifier: Modifier,
    viewModel: MainViewModel,
    code: String,
    startProcess: Boolean,
    onSurveyFinished: () -> Unit
) = AndroidView(
    factory = { context ->
        WebView(context).apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.allowContentAccess = true
            settings.allowFileAccess = true
            webViewClient = object: WebViewClient() {

                val allProgress = mutableListOf<String>()
                val scope = CoroutineScope(Dispatchers.Main)

                override fun onPageFinished(view: WebView, url: String) {
                    super.onPageFinished(view, url)
                    scope.launch {
                        Log.d("WebViewComponent", "ON::PAGE::FINISH::$url")
                        delay(1400)
                        if (allProgress.isEmpty()) {
                            view.evaluateJavascript(viewModel.getFirstPayload(code), null)
                            allProgress.add("0")
                        } else {
                            Log.d("WebViewComponent", "Enter to else")
                            view.evaluateJavascript(viewModel.payloadForExtractProgress) { consoleResult ->
                                val progressNumber = consoleResult.clearNumber()
                                if (!consoleResult.contains("null") && !allProgress.contains(progressNumber)) {
                                    val payload = viewModel.payloadByProgress[progressNumber]
                                    if (payload != null) {
                                        view.evaluateJavascript(payload, null)
                                        if (progressNumber == "11") {
                                            runBlocking { delay(1000) }
                                            view.evaluateJavascript(viewModel.payloadButtonNext, null)
                                        }
                                    }
                                    else Log.e("WebViewComponent", "PAYLOAD::NOT::FOUND::$progressNumber")
                                    allProgress.add(progressNumber)
                                } else {
                                    if (allProgress.contains("88")) viewModel.addSurvey()
                                    allProgress.clear()
                                    onSurveyFinished()
                                }
                            }
                        }

                        Log.d("WebViewComponent", "All::PROCESS::$allProgress")
                    }
                }

            }
            clearCache(true)
            if (startProcess) loadUrl(Constants.BASE_URL)
        }
    },
    modifier = modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp)
        .clip(RoundedCornerShape(15.dp))
)
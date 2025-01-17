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
import dev.dr10.autowalsurvey.ui.viewModel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
                        delay(1200)
                        view.evaluateJavascript(viewModel.payloadForExtractProgress) { consoleResult ->
                            val number = Regex("\\d+").find(consoleResult)?.value
                            if (number != null && allProgress.contains(number).not() && number != "38") {
                                Log.d("WebViewComponent", "CONSOLE::RESULT::$number")
                                if (number == "0") view.evaluateJavascript(viewModel.getFirstPayload(code), null)
                                else {
                                    val payload = viewModel.payloadByProgress[number]
                                    if (payload != null) view.evaluateJavascript(payload, null)
                                }
                                allProgress.add(number)
                            } else {
                                view.evaluateJavascript(viewModel.payloadForVerifiedCompleted) { _ ->
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
package dev.dr10.autowalsurvey.ui.components

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.dr10.autowalsurvey.domain.model.ImageInfoModel

@Composable
fun PendingImagesComponent(
    images: List<ImageInfoModel>,
    context: Context,
    onUpdateCode: (Uri, String) -> Unit,
    onDelete: (ImageInfoModel) -> Unit
) {
    println("IMAGES::PENDING::$images")
    if (images.isNotEmpty()) {
        LazyRow(modifier = Modifier.fillMaxWidth()) {
            items(images) { info ->
                PendingImageItem(
                    info,
                    context,
                    onUpdateCode = { newCode -> onUpdateCode(info.uri, newCode) },
                    onDelete = { onDelete(info) }
                )
            }
        }
    }
}
package dev.dr10.autowalsurvey.ui.components

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import dev.dr10.autowalsurvey.domain.utils.ImageUtils
import dev.dr10.autowalsurvey.ui.viewModel.MainViewModel

@Composable
fun ImagePreviewComponent(image: Bitmap?) {
    if (image != null) {
        Image(
            bitmap = image.asImageBitmap(),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(horizontal = 12.dp)
                .clip(RoundedCornerShape(15.dp)),
        )

        Spacer(modifier = Modifier.height(8.dp))
    }
}
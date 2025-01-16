package dev.dr10.autowalsurvey.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import dev.dr10.autowalsurvey.R
import dev.dr10.autowalsurvey.ui.viewModel.MainViewModel

@Composable
fun CodeActionsComponent(
    value: String,
    isProcessing: Boolean,
    onValueChange: (String) -> Unit,
    onCameraLauncher: () -> Unit,
    onGalleryLauncher: () -> Unit,
    onStartProcess: () -> Unit,
    onReloadPage: () -> Unit
) = ConstraintLayout(
    modifier = Modifier
        .fillMaxWidth()
        .height(55.dp)
        .padding(horizontal = 12.dp)
        .background(MaterialTheme.colorScheme.surfaceBright, shape = RoundedCornerShape(15.dp))
) {
    val (pickImageBtn, takePhotoBtn, startProcessBtn, codeTextField) = createRefs()

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle(
            fontSize = 15.sp,
            color = Color(0xFFDFDFDF)
        ),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
        modifier = Modifier
            .constrainAs(codeTextField) {
                width = Dimension.fillToConstraints
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start, 10.dp)
                end.linkTo(pickImageBtn.start, 10.dp)
            }
    )

    Box(
        modifier = Modifier
            .size(40.dp)
            .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(15.dp))
            .clickable { onGalleryLauncher() }
            .constrainAs(pickImageBtn) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(takePhotoBtn.start, 10.dp)
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_gallery),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.surface,
            modifier = Modifier.size(25.dp)
        )
    }

    Box(
        modifier = Modifier
            .size(40.dp)
            .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(15.dp))
            .clickable { onCameraLauncher() }
            .constrainAs(takePhotoBtn) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(startProcessBtn.start, 10.dp)
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_camera),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.surface,
            modifier = Modifier.size(25.dp)
        )
    }

    Box(
        modifier = Modifier
            .size(40.dp)
            .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(15.dp))
            .clickable { if (isProcessing) onReloadPage() else onStartProcess() }
            .constrainAs(startProcessBtn) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end, 10.dp)
            },
        contentAlignment = Alignment.Center
    ) {
        if (isProcessing) {
            Icon(
                painter = painterResource(R.drawable.ic_reaload),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.surface,
                modifier = Modifier.size(25.dp)
            )
        } else {
            Icon(
                painter = painterResource(R.drawable.ic_next),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.surface,
                modifier = Modifier.size(25.dp)
            )
        }
    }

}
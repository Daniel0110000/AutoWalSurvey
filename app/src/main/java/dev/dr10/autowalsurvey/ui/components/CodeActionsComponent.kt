package dev.dr10.autowalsurvey.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import dev.dr10.autowalsurvey.R
import dev.dr10.autowalsurvey.ui.theme.AppTheme

@Composable
fun CodeActionsComponent(
    counter: Int,
    isProcessing: Boolean,
    onCameraLauncher: () -> Unit,
    onGalleryLauncher: () -> Unit,
    onStartProcess: () -> Unit,
    onRestartState: () -> Unit,
    onResetCounter: () -> Unit
) = ConstraintLayout(
    modifier = Modifier
        .fillMaxWidth()
        .height(55.dp)
        .padding(horizontal = 12.dp)
        .background(AppTheme.colors.onBackground, shape = RoundedCornerShape(15.dp))
) {
    val (appIcon, counterBtn, pickImageBtn, takePhotoBtn, startProcessBtn) = createRefs()

    Icon(
        painter = painterResource(R.drawable.ic_app),
        contentDescription = "",
        tint = AppTheme.colors.complementary,
        modifier = Modifier
            .size(25.dp)
            .constrainAs(appIcon) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start, 10.dp)
            }
    )

    Box(
        modifier = Modifier
            .size(40.dp)
            .background(AppTheme.colors.complementary, shape = RoundedCornerShape(15.dp))
            .clickable { onResetCounter() }
            .constrainAs(counterBtn) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(pickImageBtn.start, 10.dp)
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = counter.toString(),
            color = AppTheme.colors.onBackground,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }

    Box(
        modifier = Modifier
            .size(40.dp)
            .background(AppTheme.colors.complementary, shape = RoundedCornerShape(15.dp))
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
            tint = AppTheme.colors.onBackground,
            modifier = Modifier.size(25.dp)
        )
    }

    Box(
        modifier = Modifier
            .size(40.dp)
            .background(AppTheme.colors.complementary, shape = RoundedCornerShape(15.dp))
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
            tint = AppTheme.colors.onBackground,
            modifier = Modifier.size(25.dp)
        )
    }

    Box(
        modifier = Modifier
            .size(40.dp)
            .background(AppTheme.colors.complementary, shape = RoundedCornerShape(15.dp))
            .clickable { if (isProcessing) onRestartState() else onStartProcess() }
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
                tint = AppTheme.colors.onBackground,
                modifier = Modifier.size(25.dp)
            )
        } else {
            Icon(
                painter = painterResource(R.drawable.ic_next),
                contentDescription = "",
                tint = AppTheme.colors.onBackground,
                modifier = Modifier.size(25.dp)
            )
        }
    }

}
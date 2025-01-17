package dev.dr10.autowalsurvey.ui.components

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import dev.dr10.autowalsurvey.R
import dev.dr10.autowalsurvey.domain.model.ImageInfoModel
import dev.dr10.autowalsurvey.domain.utils.toBitmap
import dev.dr10.autowalsurvey.ui.theme.AppTheme

@Composable
fun PendingImageItem(
    info: ImageInfoModel,
    context: Context,
    onUpdateCode: (String) -> Unit,
    onDelete: () -> Unit
) {
    val newCode = remember(info.code) { mutableStateOf(info.code) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Spacer(modifier = Modifier.width(12.dp))


    ConstraintLayout(
        modifier = Modifier
            .height(85.dp)
            .width(200.dp)
            .background(AppTheme.colors.onBackground, shape = RoundedCornerShape(15.dp))
    ) {

        val (image, codeTextField, deleteButton) = createRefs()

        Image(
            bitmap = info.uri.toBitmap(context).asImageBitmap(),
            contentDescription = "",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .height(55.dp)
                .clip(RoundedCornerShape(15.dp))
                .constrainAs(image) {
                    width = Dimension.fillToConstraints
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        BasicTextField(
            value = newCode.value,
            onValueChange = { newCode.value = it },
            textStyle = TextStyle(
                fontSize = 18.sp,
                color = AppTheme.colors.text,
                fontWeight = FontWeight.Bold
            ),
            cursorBrush = SolidColor(AppTheme.colors.complementary),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
                capitalization = KeyboardCapitalization.Characters
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onUpdateCode(newCode.value)
                    keyboardController?.hide()
                }
            ),
            modifier = Modifier
                .constrainAs(codeTextField) {
                    width = Dimension.fillToConstraints
                    top.linkTo(image.bottom, 5.dp)
                    start.linkTo(parent.start, 8.dp)
                    end.linkTo(parent.end, 8.dp)
                }

        )

        Icon(
            painter = painterResource(R.drawable.ic_delete),
            contentDescription = "",
            tint = AppTheme.colors.background,
            modifier = Modifier
                .size(20.dp)
                .clickable { onDelete() }
                .constrainAs(deleteButton) {
                    top.linkTo(parent.top, 5.dp)
                    end.linkTo(parent.end, 5.dp)
                }
        )

    }
}
package dev.dr10.autowalsurvey.domain.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log

object ImageUtils {

    fun uriToBitmap(context: Context, uri: Uri): Bitmap? = try {
        val inputStream = context.contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(inputStream).also { inputStream?.close() }
    } catch (e: Exception) {
        Log.e("URI::TO::BITMAP", "ERROR::$e")
        null
    }

}
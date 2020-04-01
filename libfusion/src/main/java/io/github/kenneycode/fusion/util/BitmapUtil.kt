package io.github.kenneycode.fusion.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import java.io.IOException

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * bitmap 相关 utils
 *
 */

class BitmapUtil {

    companion object {

        lateinit var context: Context

        fun decodeBitmapFromAssets(filename: String): Bitmap? {
            val options = BitmapFactory.Options()
            options.inSampleSize = 1
            try {
                return BitmapFactory.decodeStream(context.assets.open(filename))
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return null
        }

        fun flipBitmap(bitmap: Bitmap, flipX: Boolean, flipY: Boolean): Bitmap {
            val resultBitmap = Bitmap.createBitmap(
                bitmap,
                0,
                0,
                bitmap.width,
                bitmap.height,
                Matrix().apply {
                    setScale(
                        if (flipX) { -1f } else { 1f },
                        if (flipY) { -1f } else { 1f }
                    )
                },
                false
            )
            return if (resultBitmap != bitmap) {
                bitmap.recycle()
                resultBitmap
            } else {
                bitmap
            }

        }
    }

}
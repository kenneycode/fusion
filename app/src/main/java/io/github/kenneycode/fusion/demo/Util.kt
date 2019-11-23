package io.github.kenneycode.fusion.demo

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.IOException

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * utils
 *
 */

class Util {

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

    }

}
package io.github.kenneycode.fusion.output

import android.graphics.Bitmap
import io.github.kenneycode.fusion.input.InputReceiver
import io.github.kenneycode.fusion.texture.Texture
import io.github.kenneycode.fusion.util.GLUtil

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * bitmap输出
 *
 */

class FusionBitmap : InputReceiver {

    var bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)

    override fun onInputReady(input: Texture, data: MutableMap<String, Any>) {
        bitmap = GLUtil.texture2Bitmap(input)
    }

}
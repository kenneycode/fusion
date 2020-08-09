package io.github.kenneycode.fusion.parameter

import android.graphics.Bitmap
import android.opengl.GLES20.*
import android.util.Log
import io.github.kenneycode.fusion.util.GLUtil
import io.github.kenneycode.fusion.util.Util

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * Bitmap shader uniform sampler2D 参数
 *
 */

class BitmapTexture2DParameter(key: String, private var value: Bitmap, index: Int) : Texture2DParameter(key, 0, index) {

    override fun onBind(location: Int) {
        Util.assert(!value.isRecycled, "bitmap is recycled")
        super.update(GLUtil.bitmap2Texture(value, true))
        super.onBind(location)
    }

    override fun update(value: Any) {
        (value as? Bitmap)?.let {
            if (this.value != it) {
                GLUtil.deleteTexture(getValue() as Int)
            }
            this.value = it
        }
    }
}

package io.github.kenneycode.fusion.renderer

import android.graphics.Bitmap
import io.github.kenneycode.fusion.common.Constants
import io.github.kenneycode.fusion.common.glCheck
import io.github.kenneycode.fusion.util.GLUtil

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * LUT renderer
 *
 */

class LUTRenderer : SimpleRenderer(Constants.SIMPLE_VERTEX_SHADER, Constants.LUT_FRAGMENT_SHADER) {

    companion object {

        class ParameterKey {

            companion object {
                val STRENGTH = "strength"
                val LUT_TEXTURE = "u_lutTexture"
            }

        }

    }

    private lateinit var lutBitmap: Bitmap

    override fun init() {
        super.init()
        glCheck { setUniformFloat(ParameterKey.STRENGTH, 1f) }
        glCheck { setUniformTexture2D(ParameterKey.LUT_TEXTURE, GLUtil.bitmap2Texture(lutBitmap), 1) }
    }

    fun setLUTImage(lutBitmap: Bitmap) {
        this.lutBitmap = lutBitmap
    }

    fun setLUTStrength(strength: Float) {
        glCheck { setUniformFloat(ParameterKey.STRENGTH, strength) }
    }

}
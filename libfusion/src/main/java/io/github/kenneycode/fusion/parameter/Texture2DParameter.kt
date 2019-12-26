package io.github.kenneycode.fusion.parameter

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
 * Shader uniform sampler2D数组参数
 *
 */

class Texture2DParameter(key: String, private var value: Int) : UniformParameter(key) {

    override fun onBind(location: Int) {
        Util.assert(glIsTexture(value))
        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, value)
        glUniform1i(location, 0)
    }

    override fun update(value: Any) {
        (value as? Int)?.let {
            this.value = it
        }
    }
}

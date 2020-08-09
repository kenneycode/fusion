package io.github.kenneycode.fusion.parameter

import android.opengl.GLES20.*
import android.util.Log
import io.github.kenneycode.fusion.common.glCheck
import io.github.kenneycode.fusion.util.GLUtil
import io.github.kenneycode.fusion.util.Util

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * Shader uniform sampler2D参数
 *
 */

open class Texture2DParameter(key: String, private var value: Int, private val index: Int) : UniformParameter(key) {

    override fun onBind(location: Int) {
        glCheck { glActiveTexture(GL_TEXTURE0 + index) }
        glCheck { glBindTexture(GL_TEXTURE_2D, value) }
            glCheck { glUniform1i(location, index) }
    }

    override fun getValue(): Any {
        return value
    }

    override fun update(value: Any) {
        (value as? Int)?.let {
            this.value = it
        }
    }
}

package io.github.kenneycode.fusion.parameter

import android.opengl.GLES20.*

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * Shader float参数
 *
 */

class FloatParameter(key: String, private var value: Float) : UniformParameter(key) {

    override fun onBind(location: Int) {
        glUniform1f(location, value)
    }

    override fun update(value: Any) {
        (value as? Float)?.let {
            this.value = it
        }
    }

}

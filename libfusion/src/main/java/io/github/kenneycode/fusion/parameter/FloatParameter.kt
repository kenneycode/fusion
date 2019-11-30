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

class FloatParameter(key: String, private var value: Float) : Parameter(key) {

    override fun bindUniform(program: Int) {
        if (location < 0) {
            location = glGetUniformLocation(program, key)
        }
        glUniform1f(location, value)
    }

    override fun updateValue(value: Any) {
        this.value = value as Float
    }

}

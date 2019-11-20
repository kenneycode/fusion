package io.github.kenneycode.fusion.parameter

import android.opengl.GLES20.glGetUniformLocation
import android.opengl.GLES20.glUniform1i

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * Shader int参数
 *
 */

class IntParameter(key: String, private var value: Int) : Parameter(key) {

    override fun bindUniform(program: Int) {
        if (location < 0) {
            location = glGetUniformLocation(program, key)
        }
        glUniform1i(location, value)
    }

    override fun updateValue(value: Any) {
        this.value = value as Int
    }

}

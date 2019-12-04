package io.github.kenneycode.fusion.parameter

import android.opengl.GLES30

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * 4*4 Matrix 参数
 *
 */

class Mat4Parameter(key : String, private var value : FloatArray) : Parameter(key) {

    override fun bindUniform(program: Int) {
        if (location < 0) {
            location = GLES30.glGetUniformLocation(program, key)
        }
        GLES30.glUniformMatrix4fv(location, 1, false, value, 0)
    }

    override fun updateValue(value: Any) {
        this.value = value as FloatArray
    }

}
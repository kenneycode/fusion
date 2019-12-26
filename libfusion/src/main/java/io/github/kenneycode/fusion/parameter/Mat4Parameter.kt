package io.github.kenneycode.fusion.parameter

import android.opengl.GLES30
import android.util.Log

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * 4*4 Matrix 参数
 *
 */

class Mat4Parameter(key : String, private var value : FloatArray) : UniformParameter(key) {

    override fun onBind(location: Int) {
        GLES30.glUniformMatrix4fv(location, 1, false, value, 0)
    }

    override fun update(value: Any) {
        (value as? FloatArray)?.let {
            this.value = it
        }
    }

}
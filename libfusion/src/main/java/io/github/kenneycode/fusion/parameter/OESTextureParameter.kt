package io.github.kenneycode.fusion.parameter

import android.opengl.GLES11Ext
import android.opengl.GLES20.*
import android.opengl.GLES30
import io.github.kenneycode.fusion.common.glCheck

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * OES纹理参数
 *
 */

class OESTextureParameter(key : String, private var value : Int) : UniformParameter(key) {

    override fun onBind(location: Int) {
        glCheck { glActiveTexture(GLES30.GL_TEXTURE0) }
        glCheck { glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, value) }
        glCheck { glUniform1i(location, 0) }
    }

    override fun update(value: Any) {
        (value as? Int)?.let {
            this.value = it
        }
    }

}
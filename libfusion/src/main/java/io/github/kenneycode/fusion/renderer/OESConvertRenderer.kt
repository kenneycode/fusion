package io.github.kenneycode.fusion.renderer

import io.github.kenneycode.fusion.common.Constants
import io.github.kenneycode.fusion.common.DataKeys
import io.github.kenneycode.fusion.common.glCheck

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * OES纹理转TEXTURE_2D纹理
 *
 */

class OESConvertRenderer : SimpleRenderer(Constants.OES_VERTEX_SHADER, Constants.OES_FRAGMENT_SHADER) {

    override fun bindInput() {
        glCheck { setUniformOESTexture("u_texture", input.first().texture) }
    }

    override fun update(data: MutableMap<String, Any>): Boolean {
        glCheck { setUniformMat4("u_stMatrix", data[DataKeys.ST_MATRIX] as? FloatArray ?: Constants.IDENTITY_MATRIX) }
        return true
    }

}
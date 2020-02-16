package io.github.kenneycode.fusion.renderer

import io.github.kenneycode.fusion.common.Constants
import io.github.kenneycode.fusion.common.DataKeys

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * OES纹理转RGBA纹理
 *
 */

class OES2RGBARenderer : SimpleRenderer(Constants.OES_VERTEX_SHADER, Constants.OES_FRAGMENT_SHADER) {

    override fun bindInput() {
        setUniformOESTexture("u_texture", input.first().texture)
    }

    override fun update(data: MutableMap<String, Any>): Boolean {
        setUniformMat4("u_stMatrix", data[DataKeys.ST_MATRIX] as FloatArray? ?: Constants.IDENTITY_MATRIX)
        return true
    }

}
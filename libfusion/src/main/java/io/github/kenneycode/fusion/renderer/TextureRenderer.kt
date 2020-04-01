package io.github.kenneycode.fusion.renderer

import android.graphics.Bitmap
import io.github.kenneycode.fusion.texture.Texture

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * 纹理renderer
 *
 */

class TextureRenderer : SimpleRenderer() {

    fun setBitmap(bitmap: Bitmap) {
        setUniformBitmapTexture2D("u_texture", bitmap)
    }

    fun setTexture(texture: Texture) {
        setUniformTexture2D("u_texture", texture.texture)
    }

    /**
     *
     * 设置从 texture 中取内容的 rect
     * 坐标轴及坐标范围与纹理坐标系相同
     *
     * @param left 左边坐标
     * @param top 上边坐标
     * @param right 右边坐标
     * @param bottom 下边坐标
     *
     */
    fun setTextureRect(left: Float, top: Float, right: Float, bottom: Float) {
        setTextureCoordinates(floatArrayOf(left, bottom, left, top, right, top, left, bottom, right, top, right, bottom))
    }

    /**
     *
     * 设置渲染到 frame buffer 的 rect
     * 坐标轴及坐标范围与顶点坐标系相同
     *
     * @param left 左边坐标
     * @param top 上边坐标
     * @param right 右边坐标
     * @param bottom 下边坐标
     *
     */
    fun setRenderRect(left: Float, top: Float, right: Float, bottom: Float) {
        setPositions(floatArrayOf(left, bottom, left, top, right, top, left, bottom, right, top, right, bottom))
    }

    override fun beforeRender() {

    }

    override fun bindInput() {

    }

    override fun bindOutput() {
        getOutput()?.decreaseRef()
        setOutput(input.first().apply { increaseRef() })
        super.bindOutput()
    }

}
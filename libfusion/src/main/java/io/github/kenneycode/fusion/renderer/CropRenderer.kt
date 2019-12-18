package io.github.kenneycode.fusion.renderer

import android.graphics.RectF

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * 裁剪renderer
 *
 */

class CropRenderer : SimpleRenderer() {

    private val cropRect = RectF(0f, 1f, 1f, 0f)

    /**
     *
     * 设置纹理裁剪框（在纹理坐标系下）
     *
     * @param left 裁剪框左坐标
     * @param top 裁剪框上坐标
     * @param right 裁剪框右坐标
     * @param bottom 裁剪框下坐标
     *
     */
    fun setCropRect(left: Float, top: Float, right: Float, bottom: Float) {
        cropRect.left = left
        cropRect.top = top
        cropRect.right = right
        cropRect.bottom = bottom
    }

    override fun bindParameters() {
        setTextureCoordinates(floatArrayOf(cropRect.left, cropRect.bottom, cropRect.left, cropRect.top, cropRect.right, cropRect.top, cropRect.left, cropRect.bottom, cropRect.right, cropRect.top, cropRect.right, cropRect.bottom))
        super.bindParameters()
    }

}
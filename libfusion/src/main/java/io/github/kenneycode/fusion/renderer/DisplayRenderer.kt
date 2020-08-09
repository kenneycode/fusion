package io.github.kenneycode.fusion.renderer

import android.graphics.RectF
import android.opengl.GLES20.*
import io.github.kenneycode.fusion.common.Constants
import io.github.kenneycode.fusion.common.DataKeys
import io.github.kenneycode.fusion.common.glCheck

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * 显示渲染器，渲染到0号FrameBuffer，通常是屏幕
 *
 */

class DisplayRenderer(vertexShader: String = Constants.MVP_VERTEX_SHADER, fragmentShader: String = Constants.SIMPLE_FRAGMENT_SHADER) : SimpleRenderer(vertexShader, fragmentShader) {

    enum class DisplayMode {

        CenterCrop,
        FitCenter

    }

    private var displayWidth: Int = 0
    private var displayHeight: Int = 0
    private var displayMode = DisplayMode.CenterCrop

    fun setDisplaySize(width: Int, height: Int) {
        displayWidth = width
        displayHeight = height
    }

    fun setDisplayMode(displayMode: DisplayMode) {
        this.displayMode = displayMode
    }

    override fun update(data: MutableMap<String, Any>): Boolean {
        setDisplaySize(data[DataKeys.KEY_DISPLAY_WIDTH] as Int, data[DataKeys.KEY_DISPLAY_HEIGHT] as Int)
        return true
    }

    override fun bindParameters() {
        val inputWidth = input.first().width
        val inputHeight = input.first().height
        val displayRect = RectF(-1f, 1f, 1f, -1f)
        val textureRect = RectF(0f, 1f, 1f, 0f)
        val inputRatio = inputWidth.toFloat() / inputHeight
        val displayRatio = displayWidth.toFloat() / displayHeight
        when (displayMode) {
            DisplayMode.CenterCrop -> {
                if (inputRatio < displayRatio) {
                    textureRect.left = 0f
                    textureRect.right = 1f
                    textureRect.top = 1 - (1 - inputRatio / displayRatio) / 2
                    textureRect.bottom = (1 - inputRatio / displayRatio) / 2
                } else {
                    textureRect.top = 1f
                    textureRect.bottom = 0f
                    textureRect.left =  (1 - 1 / inputRatio * displayRatio) / 2
                    textureRect.right = 1 - (1 - 1 / inputRatio * displayRatio) / 2
                }
            }
            DisplayMode.FitCenter -> {
                if (inputRatio < displayRatio) {
                    displayRect.bottom = -1f
                    displayRect.top = 1f
                    displayRect.left = -1 + (2f - 2f * inputRatio) / 2
                    displayRect.right = 1f - (displayRect.left - (-1))
                } else {
                    displayRect.left = -1f
                    displayRect.right = 1f
                    displayRect.bottom = -1f + (2f - 2f / inputRatio) / 2
                    displayRect.top = 1f - (displayRect.bottom - (-1))
                }
            }
        }
        setPositions(
            floatArrayOf(
                displayRect.left, displayRect.bottom,
                displayRect.left, displayRect.top,
                displayRect.right, displayRect.top,
                displayRect.left, displayRect.bottom,
                displayRect.right, displayRect.top,
                displayRect.right, displayRect.bottom
            )
        )
        setTextureCoordinates(
            floatArrayOf(
                textureRect.left, textureRect.bottom,
                textureRect.left, textureRect.top,
                textureRect.right, textureRect.top,
                textureRect.left, textureRect.bottom,
                textureRect.right, textureRect.top,
                textureRect.right, textureRect.bottom
            )
        )
        super.bindParameters()
    }

    override fun bindOutput() {
        glCheck { glBindFramebuffer(GL_FRAMEBUFFER, 0) }
        glCheck { glViewport(0, 0, displayWidth, displayHeight) }
    }

}
package io.github.kenneycode.fusion.renderer

import io.github.kenneycode.fusion.common.Constants
import io.github.kenneycode.fusion.process.RenderChain
import io.github.kenneycode.fusion.texture.Texture

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * 高斯模糊 renderer
 *
 */

class GaussianBlurRenderer : RenderChain()  {

    companion object {

        class ParameterKey {

            companion object {
                val WEIGHTS = "weights"
                val H_OFFSET = "hOffset"
                val V_OFFSET = "vOffset"
                val ORIENTATION = "orientation"
            }

        }

        private val HALF_SAMPLE_POINTS = 5

    }

    private val hRenderer = SimpleRenderer(Constants.SIMPLE_VERTEX_SHADER, Constants.GAUSSIAN_BLUR_FRAGMENT_SHADER)
    private val vRenderer = SimpleRenderer(Constants.SIMPLE_VERTEX_SHADER, Constants.GAUSSIAN_BLUR_FRAGMENT_SHADER)
    private var blurRadius = 3

    override fun init() {
        val weights = normalize(calculateWeights(5f))
        hRenderer.apply {
            setUniformInt(ParameterKey.ORIENTATION, 0)
            setUniformFloatArray(ParameterKey.WEIGHTS, weights)
            addRenderer(this)
        }
        vRenderer.apply {
            setUniformInt(ParameterKey.ORIENTATION, 1)
            setUniformFloatArray(ParameterKey.WEIGHTS, weights)
            addRenderer(this)
        }
        super.init()
    }

    /**
     *
     * 设置模糊半径
     *
     * @param blurRadius 模糊半径
     *
     */
    fun setBlurRadius(blurRadius: Int) {
        this.blurRadius = blurRadius
    }

    override fun render() {
        val inputWidth = getInput().first().width
        val inputHeight = getInput().first().height
        hRenderer.setUniformFloat(ParameterKey.H_OFFSET, blurRadius.toFloat() / inputWidth / HALF_SAMPLE_POINTS)
        hRenderer.setUniformFloat(ParameterKey.V_OFFSET, blurRadius.toFloat() / inputHeight / HALF_SAMPLE_POINTS)
        vRenderer.setUniformFloat(ParameterKey.H_OFFSET, blurRadius.toFloat() / inputWidth / HALF_SAMPLE_POINTS)
        vRenderer.setUniformFloat(ParameterKey.V_OFFSET, blurRadius.toFloat() / inputHeight / HALF_SAMPLE_POINTS)
        super.render()
    }

    private fun calculateWeights(sigma: Float): FloatArray {
        val weights = FloatArray(6)
        weights[0] = gaussianFunc(0f, 0f, sigma)
        for (i in 1 until 6) {
            weights[i] = gaussianFunc(i.toFloat(), 0f, sigma)
        }
        return weights
    }

    private fun normalize(weights: FloatArray): FloatArray {
        var sum = weights[0]
        for (i in 1 until weights.size) {
            sum += weights[i] * 2
        }
        for (i in weights.indices) {
            weights[i] /= sum
        }
        return weights
    }

    private fun gaussianFunc(x: Float, y: Float, sigma: Float): Float {
        return ((1f / (2.0 * Math.PI * sigma * sigma)) * Math.pow(Math.E, -(x * x + y * y) / (2.0 * sigma))).toFloat()
    }

}